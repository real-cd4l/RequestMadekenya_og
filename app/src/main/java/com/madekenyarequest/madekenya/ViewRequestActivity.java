package com.madekenyarequest.madekenya;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.madekenyarequest.madekenya.adapter.RequestItemsAdapter;
import com.madekenyarequest.madekenya.pojos.Item;
import com.madekenyarequest.madekenya.pojos.Request;
import com.madekenyarequest.madekenya.pojos.Subscriber;
import com.madekenyarequest.madekenya.utilities.LocalDb;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ViewRequestActivity extends AppCompatActivity {

    private static final String TAG = "ViewRequestActivity";
    private ImageButton imbShareRequest, imbSendRequest, imbDoneRequest, imbDoneAllRequest, imbCloseRequestPage;
    private TextView tvTitle, tvSubtitleTitle, tvNoCargoAttached;
    private MaterialButton mbAddCargo;
    private RecyclerView recyclerView;
    private RequestItemsAdapter requestItemsAdapter;
    private Request request;
    private Subscriber subscriber;
    private MaterialButton submitButton;
    private LinearLayout ll_show_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_RequestMadekenya_NoActionBar);
        setContentView(R.layout.activity_view_request);

        registerViews();
        if (getIntent().hasExtra("request")) {
            subscriber = LocalDb.getSavedCustomer(getApplicationContext());
            Log.d(TAG, "onCreate: subscriber string " + subscriber.toString());

            request = getIntent().getParcelableExtra("request");
            if (request != null) {

                manageToolBarIcons();
            }
        } else if (getIntent().hasExtra("subscriber")) {
            ll_show_progress.setVisibility(View.VISIBLE);
            //create new request
            subscriber = getIntent().getParcelableExtra("subscriber");
            if (subscriber != null) {
                //increment request counters
                //path requestcounter -> uid-count
                Map<String, Object> updates = new HashMap<>();
                String counterPath = "requestcounter/" + subscriber.getId() + "/count";
                updates.put(counterPath, ServerValue.increment(1));
                FirebaseDatabase.getInstance().getReference("madekenyarequest").updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //successfully , now fetch
                            FirebaseDatabase.getInstance().getReference("madekenyarequest").child(counterPath).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Long latestCount = (Long) snapshot.getValue();
                                        if (latestCount != null) {
                                            request = new Request(latestCount);
                                            manageToolBarIcons();


                                        } else {
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else if (task.isCanceled()) {
                            //toast failed
                        }
                    }
                });
            } else {
                finish();
            }
        } else {
            finish();
        }

    }

    private void logicsAndEvents() {
        tvNoCargoAttached = findViewById(R.id.tvNoCargoAttached);
        requestItemsAdapter = new RequestItemsAdapter(ViewRequestActivity.this, request.getItems(), request, submitButton, tvNoCargoAttached);
        recyclerView.setAdapter(requestItemsAdapter);

        imbCloseRequestPage.setOnClickListener(v -> finish());

        mbAddCargo.setOnClickListener(v -> {
            //open material dialog add cargo
            View root = LayoutInflater.from(ViewRequestActivity.this).inflate(R.layout.layout_dialog_add_cargo, null, false);
            RadioGroup radioGroup = root.findViewById(R.id.rgRisiti);

            EditText edJinaLaMzigo = root.findViewById(R.id.edJinaLaMzigo);
            EditText edIdadi = root.findViewById(R.id.edIdadi);
            EditText edUnapoenda = root.findViewById(R.id.edUnapoenda);

            if (request.getStatus() == Request.REQUEST_SENT) {
                submitButton.setVisibility(View.VISIBLE);
            }

            new MaterialAlertDialogBuilder(ViewRequestActivity.this, R.style.AlertDialogTheme)
                    .setView(root)
                    .setPositiveButton("Add", (dialog, which) -> {
                        if (edJinaLaMzigo.getText() != null && edIdadi.getText() != null && edUnapoenda.getText() != null) {
                            String jinaLaMzigo = edJinaLaMzigo.getText().toString();
                            String idadi = edIdadi.getText().toString();
                            String unapoenda = edUnapoenda.getText().toString();

                            if (radioGroup.isSelected()) {
                                Toast.makeText(ViewRequestActivity.this, "Je mzigo una risiti ya TRA ?", Toast.LENGTH_SHORT).show();
                            } else if (jinaLaMzigo.trim().isEmpty() || idadi.trim().isEmpty() || unapoenda.trim().isEmpty()) {
                                Toast.makeText(ViewRequestActivity.this, "Jaza nafasi zilizo wazi !!", Toast.LENGTH_SHORT).show();
                            } else {
                                boolean unaRisiti;
                                int checkedId = radioGroup.getCheckedRadioButtonId();
                                unaRisiti = checkedId == R.id.rbRisiti;
                                int idadiLong = Integer.parseInt(idadi);

                                Item item = new Item(jinaLaMzigo, idadiLong, unapoenda, unaRisiti);
                                request.getItems().add(item);
                                requestItemsAdapter.notifyItemInserted(request.getItems().size());
                                imbSendRequest.setVisibility(View.VISIBLE);
                            }
                        }
                    })
                    .setNeutralButton("close", null)
                    .show();
        });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("madekenyarequest");
        View.OnClickListener sendRequest = v -> {
            String requestKey = String.valueOf(request.getNumber());


            //increment
            String totalPendingRequestCargoPath = "subscriber/" + subscriber.getId() + "/totalPendingRequestCargo";
            reference.child(totalPendingRequestCargoPath).runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                    if (request.getItems() != null && request.getItems().size() > 0) {
                        long itemCount = 0;
                        for (Item item : request.getItems()) {
                            itemCount = itemCount + item.getIdadi();
                        }
                        if (itemCount > 0) {
                            if (currentData.getValue() == null) {
                                currentData.setValue(itemCount);
                            } else {
                                long count = (long) currentData.getValue();
                                count = count + itemCount;
                                currentData.setValue(count);
                            }
                            return Transaction.success(currentData);
                        }
                    }
                    return Transaction.abort();
                }

                @Override
                public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                    if (error == null) {
                        new Thread(() -> {
                            request.setStatus(Request.REQUEST_SENT);
                            reference.child("requests").child(subscriber.getId()).child(requestKey).setValue(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    new MaterialAlertDialogBuilder(ViewRequestActivity.this, R.style.AlertDialogTheme).setMessage("umefanikiwa kututumia maombi ya kusafirishiwa mizigo,Tunazingatia, Asante.").setPositiveButton("Sawa", null).show();
                                    finish();
                                }
                            });
                        }).start();
                    } else {
                        error.toException().printStackTrace();
                    }
                }
            });
        };
        imbSendRequest.setOnClickListener(sendRequest);
        submitButton.setOnClickListener(v -> new Thread(() -> {
            request.setStatus(Request.REQUEST_SENT);
            reference.child("requests").child(subscriber.getId()).child(String.valueOf(request.getNumber())).setValue(request).addOnSuccessListener(unused -> {
                new MaterialAlertDialogBuilder(ViewRequestActivity.this, R.style.AlertDialogTheme).setMessage("Umefanikiwa kutuma maboresho,Tumeyapokea. Asante.").setPositiveButton("Sawa", null).show();
                finish();
            });
        }).start());

        imbShareRequest.setOnClickListener(v -> {
            if (request.getCustomer() != null) {
                Intent intent = new Intent(this, DeliveryNoteView.class);
                intent.putExtra("customer", (Parcelable) request.getCustomer());
                startActivity(intent);
            }

        });

    }

    private void manageToolBarIcons() {
        tvTitle.setText(String.format(Locale.getDefault(), "Req no %,d", request.getNumber()));
        tvSubtitleTitle.setText(request.getDate());
        imbShareRequest.setVisibility(View.GONE);
        ll_show_progress.setVisibility(View.GONE);
        if (request.getItems() != null && request.getItems().size() > 0) {
            if (request.getStatus() == Request.REQUEST_PENDING) {
                mbAddCargo.setVisibility(View.VISIBLE);
                imbSendRequest.setVisibility(View.VISIBLE);
                imbShareRequest.setVisibility(View.GONE);

            } else if (request.getStatus() == Request.REQUEST_SENT) {
                imbDoneRequest.setVisibility(View.VISIBLE);
                mbAddCargo.setVisibility(View.VISIBLE);
            } else if (request.getStatus() == Request.REQUEST_RECEIVED) {
                imbDoneAllRequest.setVisibility(View.VISIBLE);
                imbShareRequest.setVisibility(View.VISIBLE);
            }
        } else {
            mbAddCargo.setVisibility(View.VISIBLE);
        }

        logicsAndEvents();
    }

    private void registerViews() {
        imbShareRequest = findViewById(R.id.imbShareRequest);
        imbSendRequest = findViewById(R.id.imbSendRequest);
        imbDoneRequest = findViewById(R.id.imbDoneRequest);
        imbDoneAllRequest = findViewById(R.id.imbDoneAllRequest);
        imbCloseRequestPage = findViewById(R.id.imbCloseRequestPage);
        mbAddCargo = findViewById(R.id.mbAddCargo);
        recyclerView = findViewById(R.id.recyclerView);
        submitButton = findViewById(R.id.mbSubmitButton);
        ll_show_progress = findViewById(R.id.ll_show_progress);
        tvSubtitleTitle = findViewById(R.id.tvSubtitleTitle);
        tvTitle = findViewById(R.id.tvTitle);
    }
}