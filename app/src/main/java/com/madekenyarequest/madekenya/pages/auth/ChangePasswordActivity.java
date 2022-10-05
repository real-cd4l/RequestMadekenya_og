package com.madekenyarequest.madekenya.pages.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.FirebaseDatabase;
import com.madekenyarequest.madekenya.R;
import com.madekenyarequest.madekenya.pojos.Subscriber;
import com.madekenyarequest.madekenya.utilities.LocalDb;

public class ChangePasswordActivity extends AppCompatActivity {
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
    private Toolbar toolBar;
    private EditText edUsername, edPassword, edNewPassword, edConfirmPassword;
    private MaterialButton mbChage;
    private Subscriber subscriber;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_RequestMadekenya_NoActionBar);
        setContentView(R.layout.activity_change_password);
        subscriber = getIntent().getParcelableExtra("subscriber");
        if (subscriber == null) {
            finish();
        } else {
            registerViews();
        }
    }

    private void registerViews() {
        toolBar = findViewById(R.id.toolBar);
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        edNewPassword = findViewById(R.id.edNewPassword);
        edConfirmPassword = findViewById(R.id.edConfirmPassword);
        mbChage = findViewById(R.id.mbChage);
        progressBar = findViewById(R.id.progressBar);

        toolBar.setNavigationOnClickListener(v -> finish());

        mbChage.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            if (edUsername.getText() != null && edPassword.getText() != null && edNewPassword.getText() != null && edConfirmPassword.getText() != null) {
                String username = edUsername.getText().toString();
                String password = edPassword.getText().toString();
                String newPassword = edNewPassword.getText().toString();
                String confirmPassword = edConfirmPassword.getText().toString();
                progressBar.setVisibility(View.VISIBLE);

                if (username.isEmpty()) {
                    Toast.makeText(this, "Please put username !!", Toast.LENGTH_SHORT).show();
                } else if (newPassword.isEmpty()) {
                    Toast.makeText(this, "Please provide new password", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(this, "Please provide password", Toast.LENGTH_SHORT).show();
                } else if (confirmPassword.isEmpty()) {
                    Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show();
                } else {
                    if (!username.equalsIgnoreCase(subscriber.getUsername()) || !password.equals(subscriber.getPassword())) {
                        Toast.makeText(this, "Wrong username or password..!", Toast.LENGTH_SHORT).show();
                    } else {
                        //change password
                        subscriber.setPassword(newPassword);
                        subscriber.setUsername_password(subscriber.getUsername() + "-" + newPassword);
                        //set online
                        FirebaseDatabase.getInstance().getReference("madekenyarequest").child("subscriber").child(subscriber.getId()).setValue(subscriber).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                LocalDb.saveCustomerLocal(getApplicationContext(), subscriber);
                                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}