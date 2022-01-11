package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        TextView titleOfNote = findViewById(R.id.titleofnote);
        TextView contentOfNote = findViewById(R.id.contentofnote);
        FloatingActionButton edit = findViewById(R.id.editnote);
        Toolbar toolbar = findViewById(R.id.toolbarofnote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String noteID = intent.getStringExtra("noteID");
        titleOfNote.setText(title);
        contentOfNote.setText(content);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteDetailsActivity.this, EditNoteActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("content", content);
                intent.putExtra("noteID", noteID);
                startActivity(intent);
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