package com.madekenyarequest.madekenya.pages.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private MaterialButton mbLogin, mbCreateAccount;
    private EditText edUsername, edPassword;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_RequestMadekenya_NoActionBar);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        registerViews();
    }

    private void registerViews() {
        mbLogin = findViewById(R.id.mbLogin);
        mbCreateAccount = findViewById(R.id.mbCreateAccount);
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        progressBar = findViewById(R.id.progressBar);

        mbLogin.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);

            if (edUsername.getText() != null && edPassword.getText() != null) {
                String username = edUsername.getText().toString();
                String password = edPassword.getText().toString();

                if (username.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Please provide username...!!", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Please provide password..!!", Toast.LENGTH_SHORT).show();
                } else {
                    String username_password = username.trim().toLowerCase() + "-" + password.trim();
                    Log.d(TAG, "onClick: username_password ==> " + username_password);

                    //search online username
                    Query query = FirebaseDatabase.getInstance().getReference("madekenyarequest").child("subscriber").orderByChild("username_password").equalTo(username_password).limitToLast(1);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    Subscriber subscriber = child.getValue(Subscriber.class);
                                    if (subscriber != null) {
                                        LocalDb.saveCustomerLocal(getApplicationContext(), subscriber);

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "problem occurred..!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "wrong username or password..!!", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }


        });

        mbCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });

    }

    private void loginAsMadekenya() {
        String ownerUsername = "owner@madekenya.com";
        String ownerPassword = "1234567";

        mAuth.signInWithEmailAndPassword(ownerUsername, ownerPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        firebaseUser = mAuth.getCurrentUser();
                    }
                }).addOnFailureListener(e -> {
                    firebaseUser = null;
                    e.printStackTrace();
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