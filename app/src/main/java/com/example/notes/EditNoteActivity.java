package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditNoteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        EditText titleOfNote = findViewById(R.id.edittitleofnote);
        EditText contentOfNote = findViewById(R.id.editcontentofnote);
        FloatingActionButton save = findViewById(R.id.savenote);
        Toolbar toolbar = findViewById(R.id.toolbarofeditnote);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String noteID = intent.getStringExtra("noteID");
        titleOfNote.setText(intent.getStringExtra("title"));
        contentOfNote.setText(intent.getStringExtra("content"));

        save.setOnClickListener(view -> {
            String title = titleOfNote.getText().toString();
            String content = contentOfNote.getText().toString();
            if(title.isEmpty() || content.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Both Fields required", Toast.LENGTH_SHORT).show();
            }else {
                DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(noteID);
                Map<String, Object> note = new HashMap<>();
                note.put("title", title);
                note.put("content", content);
                documentReference.set(note).addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(), "Note Updated Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditNoteActivity.this, NotesActivity.class));
                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed To Update Note", Toast.LENGTH_SHORT).show());
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