package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNotesActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    EditText createTitle, createContent;
    FloatingActionButton saveNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);

        Toolbar toolbar = findViewById(R.id.toolbarofcreatenote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createTitle = findViewById(R.id.createtitleofnote);
        createContent = findViewById(R.id.createcontentofnote);
        ProgressBar progressBar = findViewById(R.id.progresscreatenote);
        saveNote = findViewById(R.id.createnote);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        saveNote.setOnClickListener(view -> {
            String title = createTitle.getText().toString();
            String content = createContent.getText().toString();
            if(title.isEmpty() || content.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Both Fields required", Toast.LENGTH_SHORT).show();
            }else {
                progressBar.setVisibility(View.VISIBLE);
                DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
                Map<String, Object> note = new HashMap<>();
                note.put("title", title);
                note.put("content", content);
                documentReference.set(note).addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(), "Note Created Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateNotesActivity.this, NotesActivity.class));
                }).addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Failed To Create Note", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() ==  android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}