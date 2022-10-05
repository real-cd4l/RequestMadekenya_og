package com.madekenyarequest.madekenya.pages.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.madekenyarequest.madekenya.MainActivity;
import com.madekenyarequest.madekenya.R;
import com.madekenyarequest.madekenya.pojos.Subscriber;
import com.madekenyarequest.madekenya.utilities.LocalDb;

public class CreateAccountActivity extends AppCompatActivity {
    private static final String TAG = "CreateAccountActivity";
    private final InputFilter alphaNumericFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence charSequence, int start, int end,
                                   Spanned dest, int etStart, int etEnd) {
            if (charSequence.equals("")) {
                return charSequence;
            }
            if (charSequence.toString().matches("[A-Za-z0-9 ]+")) {
                return charSequence;
            }
            return "";
        }
    };
    InputFilter spaceFilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String stringSource = source.toString();
            String stringDest = dest.toString();
            if (stringSource.equals(" ")) {
                if (stringDest.length() == 0) {
                    return "";
                }
                if ((dstart > 0 && stringDest.charAt(dstart - 1) == ' ') || (stringDest.length() > dstart && stringDest.charAt(dstart) == ' ') || dstart == 0)
                    return "";
            }
            return null;
        }
    };
    private EditText edUsername, edLocation, edPassword, edConfirmPassword;
    private MaterialButton mbCreateAccount;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_RequestMadekenya_NoActionBar);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();
        loginAsMadekenya();
        initializeViews();


    }

    private void loginAsMadekenya() {
        String ownerUsername = "owner@madekenya.com";
        String ownerPassword = "1234567";

        mAuth.signInWithEmailAndPassword(ownerUsername, ownerPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        firebaseUser = mAuth.getCurrentUser();
                    }
                }).addOnFailureListener(e -> {
                    firebaseUser = null;
                    e.printStackTrace();
                });
    }

    private void initializeViews() {
        edUsername = findViewById(R.id.edUsername);
//        edUsername.setFilters(new InputFilter[]{alphaNumericFilter, spaceFilter});

        edUsername.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edUsername, InputMethodManager.SHOW_FORCED);

        edLocation = findViewById(R.id.edLocation);
        edPassword = findViewById(R.id.edPassword);
        edConfirmPassword = findViewById(R.id.edConfirmPassword);
        mbCreateAccount = findViewById(R.id.mbCreateAccount);
        toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationOnClickListener(v -> finish());
        mbCreateAccount.setOnClickListener(v -> {
            if (edUsername.getText() != null && edLocation.getText() != null && edPassword.getText() != null && edConfirmPassword.getText() != null) {
                String username = edUsername.getText().toString();
                String location = edLocation.getText().toString();
                String password = edPassword.getText().toString();
                String confirmPassword = edConfirmPassword.getText().toString();

                if (username.isEmpty()) {
                    Toast.makeText(this, "Please put username !!", Toast.LENGTH_SHORT).show();
                } else if (location.isEmpty()) {
                    Toast.makeText(this, "Please provide location", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(this, "Please provide password", Toast.LENGTH_SHORT).show();
                } else if (confirmPassword.isEmpty()) {
                    Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(CreateAccountActivity.this, "please wait..!!", Toast.LENGTH_SHORT).show();
                    //search online username
                    Query usernameQuery = FirebaseDatabase.getInstance().getReference("madekenyarequest").child("subscriber").orderByChild("username").equalTo(username);

                    usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Toast.makeText(CreateAccountActivity.this, "Username exists", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CreateAccountActivity.this, "not exists", Toast.LENGTH_SHORT).show();
                                //now add new customer
                                final String uid = FirebaseDatabase.getInstance().getReference("madekenyarequest").child("subscriber").push().getKey();
                                if (uid != null) {
                                    Subscriber customer = new Subscriber(uid, username, location, password, firebaseUser.getUid());
                                    FirebaseDatabase.getInstance().getReference("madekenyarequest").child("subscriber").child(uid).setValue(customer).addOnSuccessListener(unused -> {
                                        LocalDb.saveCustomerLocal(getApplicationContext(), customer);
                                        Toast.makeText(CreateAccountActivity.this, "Successfully", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();

                                    }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed..!!", Toast.LENGTH_SHORT).show());
                                } else {
                                    Toast.makeText(CreateAccountActivity.this, "Failed..!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG, "onCancelled: failed");
                            Toast.makeText(CreateAccountActivity.this, "failed....", Toast.LENGTH_SHORT).show();
                            error.toException().printStackTrace();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            loginAsMadekenya();
        }
    }
}