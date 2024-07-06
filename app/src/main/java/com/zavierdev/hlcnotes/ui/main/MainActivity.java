package com.zavierdev.hlcnotes.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zavierdev.hlcnotes.R;
import com.zavierdev.hlcnotes.model.Note;
import com.zavierdev.hlcnotes.ui.create.CreateUpdateNoteActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvNotes;
    private TextView tvEmptyNotes;
    private FloatingActionButton fabAddNote;

    private NotesRecyclerViewAdapter rvNotesAdapter;
    private MainActivityViewModel viewModel;

    private final ActivityResultLauncher<Intent> mCreateEditNoteLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            boolean resultData = data.getBooleanExtra(CreateUpdateNoteActivity.RESULT_TAG, false);
            if (resultData) {
                updateNotes();
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            initViewModel();
            initViews(v);
            return insets;
        });
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
    }

    private void initViews(View v) {
        rvNotes = v.findViewById(R.id.rv_notes);
        rvNotesAdapter = new NotesRecyclerViewAdapter();
        tvEmptyNotes = v.findViewById(R.id.tv_empty_notes);
        fabAddNote = v.findViewById(R.id.fab_add_note);

        rvNotes.setAdapter(rvNotesAdapter);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotesAdapter.setOnItemClickListener(note -> {
            startEditNoteActivity(note);
        });

        updateNotes();

        fabAddNote.setOnClickListener(v1 -> {
            startCreateNoteActivity();
        });
    }

    private void updateNotes() {
        List<Note> notes = viewModel.getNotes();
        if (notes.isEmpty()) {
            rvNotes.setVisibility(View.GONE);
            tvEmptyNotes.setVisibility(View.VISIBLE);
        }
        rvNotesAdapter.updateNotes(notes);
    }

    private void startCreateNoteActivity() {
        Intent intent = new Intent(MainActivity.this, CreateUpdateNoteActivity.class);
        mCreateEditNoteLauncher.launch(intent);
    }

    private void startEditNoteActivity(Note note) {
        Intent intent = new Intent(MainActivity.this, CreateUpdateNoteActivity.class);
        intent.putExtra(CreateUpdateNoteActivity.EXTRA_NOTE, note);
        mCreateEditNoteLauncher.launch(intent);
    }
}