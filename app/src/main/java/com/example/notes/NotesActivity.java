package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.location.GnssAntennaInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<FirestoreModel, NoteViewHolder> noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        ProgressBar progressBar = findViewById(R.id.progressnotes);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.recyclerView);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        StaggeredGridLayoutManager staggeredGridLayoutManager;
        getSupportActionBar().setTitle("All Notes");
        FloatingActionButton floatingActionButton = findViewById(R.id.createNoteFab);
        floatingActionButton.setOnClickListener(view -> {
            startActivity(new Intent(NotesActivity.this, CreateNotesActivity.class));
        });
        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<FirestoreModel> allUserNotes = new FirestoreRecyclerOptions.Builder<FirestoreModel>().setQuery(query, FirestoreModel.class).build();
        noteAdapter = new FirestoreRecyclerAdapter<FirestoreModel, NoteViewHolder>(allUserNotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull @NotNull NoteViewHolder holder, int position, @NonNull @NotNull FirestoreModel model) {
                progressBar.setVisibility(View.GONE);
                int color = getRandCol();
                holder.note.setBackgroundColor(holder.note.getResources().getColor(color));
                holder.noteTitle.setText(model.getTitle());
                holder.noteContent.setText(model.getContent());
                String docID = noteAdapter.getSnapshots().getSnapshot(position).getId();
                holder.popBtn.setOnClickListener(view -> {
                    PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                    popupMenu.setGravity(Gravity.END);
                    popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            Intent intent = new Intent(view.getContext(), EditNoteActivity.class);
                            intent.putExtra("title", model.getTitle());
                            intent.putExtra("content", model.getContent());
                            intent.putExtra("noteID", docID);
                            view.getContext().startActivity(intent);
                            return false;
                        }
                    });
                    popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docID);
                            documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Failed To Delete Note", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return false;
                        }
                    });
                    popupMenu.show();
                });
                holder.note.setOnClickListener(view -> {
                    Intent intent = new Intent(NotesActivity.this, NoteDetailsActivity.class);
                    intent.putExtra("title", model.getTitle());
                    intent.putExtra("content", model.getContent());
                    intent.putExtra("noteID", docID);
                    startActivity(intent);
                });
            }

            @NonNull
            @NotNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                return new NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout, parent, false));
            }
        };
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(noteAdapter);
    }

    private int getRandCol() {
        List<Integer> cols = new ArrayList<>();
        cols.add(R.color.col1);
        cols.add(R.color.col2);
        cols.add(R.color.col3);
        cols.add(R.color.col4);
        cols.add(R.color.col5);
        cols.add(R.color.col6);
        cols.add(R.color.col7);
        cols.add(R.color.col8);
        cols.add(R.color.col9);
        cols.add(R.color.col10);
        cols.add(R.color.col11);
        cols.add(R.color.col12);
        cols.add(R.color.col13);
        cols.add(R.color.col14);
        cols.add(R.color.col15);
        cols.add(R.color.col16);
        cols.add(R.color.col17);
        cols.add(R.color.col18);
        cols.add(R.color.col19);
        cols.add(R.color.col20);
        Random random = new Random();
        int index = random.nextInt(cols.size());
        return cols.get(index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(NotesActivity.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter != null) {
            noteAdapter.startListening();
        }
    }
}