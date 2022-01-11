package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity {

    private EditText forgotEmail;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        getSupportActionBar().hide();
        forgotEmail = findViewById(R.id.forgotEmail);
        Button createNewPass = findViewById(R.id.createNewPass);
        TextView goBack = findViewById(R.id.goBack);
        firebaseAuth = FirebaseAuth.getInstance();

        goBack.setOnClickListener(v -> startActivity(new Intent(ForgotPassActivity.this, MainActivity.class)));

        createNewPass.setOnClickListener(v -> {
            String mail = forgotEmail.getText().toString().trim();
            if(mail.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Email required", Toast.LENGTH_SHORT).show();
            }else {
                // send an email
                firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Password Recover Mail sent", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(ForgotPassActivity.this, MainActivity.class));
                    }else {
                        Toast.makeText(getApplicationContext(), "Email/Account Doesn't Exist", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}