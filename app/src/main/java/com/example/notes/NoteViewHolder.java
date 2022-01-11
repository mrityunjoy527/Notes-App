package com.example.notes;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    TextView noteTitle, noteContent;
    ImageView popBtn;
    LinearLayout note;
    public NoteViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        note = itemView.findViewById(R.id.note);
        noteTitle = itemView.findViewById(R.id.noteTitle);
        noteContent = itemView.findViewById(R.id.noteContent);
        popBtn = itemView.findViewById(R.id.menuPop);
    }
}
