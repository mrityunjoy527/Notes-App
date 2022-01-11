package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText loginUpEmail;
    private EditText loginUpPass;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        RelativeLayout newLogin = findViewById(R.id.newLogin);
        loginUpEmail = findViewById(R.id.loginEmail);
        loginUpPass = findViewById(R.id.loginPass);
        RelativeLayout logIn = findViewById(R.id.logIn);
        TextView forgotPass = findViewById(R.id.forgotPass);
        progressBar = findViewById(R.id.progress);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null) {
            finish();
            startActivity(new Intent(MainActivity.this, NotesActivity.class));
        }
        forgotPass.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ForgotPassActivity.class)));
        newLogin.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SignUpActivity.class)));
        logIn.setOnClickListener(v -> {
            String mail = loginUpEmail.getText().toString().trim();
            String pass = loginUpPass.getText().toString().trim();
            if (mail.isEmpty() || pass.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Email and Password required", Toast.LENGTH_SHORT).show();
            }else if (pass.length() < 8) {
                Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
            } else {
                // login
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        checkEmailVerification();
                    }else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Account Doesn't Exist\nOr, Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void checkEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null && firebaseUser.isEmailVerified()) {
            Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, NotesActivity.class));
        }else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Email isn't Verified", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}