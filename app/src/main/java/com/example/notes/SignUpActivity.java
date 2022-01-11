package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText signUpEmail;
    private EditText signUpPass;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();
        RelativeLayout signUp = findViewById(R.id.signUp);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPass = findViewById(R.id.signUpPass);
        TextView goBack = findViewById(R.id.goBack);
        firebaseAuth = FirebaseAuth.getInstance();

        goBack.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, MainActivity.class)));

        signUp.setOnClickListener(v -> {
            String mail = signUpEmail.getText().toString().trim();
            String pass = signUpPass.getText().toString().trim();
            if(mail.isEmpty() || pass.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Email and Password required", Toast.LENGTH_SHORT).show();
            } else if (pass.length() < 8){
                Toast.makeText(getApplicationContext(), "Password length should be greater than 7", Toast.LENGTH_SHORT).show();
            } else {
                // sign up
                firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                        sendVerificationEmail();
                    }else Toast.makeText(getApplicationContext(), "Failed To Register", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    private void sendVerificationEmail() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(task -> {
                Toast.makeText(getApplicationContext(), "Verification Email Sent, Login After Verifying It", Toast.LENGTH_LONG).show();
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            });
        }
        else Toast.makeText(this, "Failed To Send Verification Email", Toast.LENGTH_SHORT).show();
    }
}