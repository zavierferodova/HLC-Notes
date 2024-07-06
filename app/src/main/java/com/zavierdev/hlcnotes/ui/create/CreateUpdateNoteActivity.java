package com.zavierdev.hlcnotes.ui.create;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zavierdev.hlcnotes.R;
import com.zavierdev.hlcnotes.model.Note;
import com.zavierdev.hlcnotes.ui.main.MainActivity;

public class CreateUpdateNoteActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE = "extra_note";
    public static final String RESULT_TAG = "result";
    private EditText edtTitle;
    private EditText edtContent;
    private Button btnDeleteNote;

    private FloatingActionButton fabSave;

    private CreateUpdateViewModel createUpdateViewModel;
    private Note noteForEdit = null;

    private boolean noteInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_update_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            initViewModel();
            initViews(v);
            return insets;
        });
    }

    private void initViewModel() {
        createUpdateViewModel = new ViewModelProvider(this).get(CreateUpdateViewModel.class);
    }

    private void initViews(View v) {
        edtTitle = v.findViewById(R.id.edt_title);
        edtContent = v.findViewById(R.id.edt_content);
        btnDeleteNote = v.findViewById(R.id.btn_delete_note);
        fabSave = v.findViewById(R.id.fab_save_note);

        btnDeleteNote.setOnClickListener(v1 -> {
            showDeleteDialog();
        });

        fabSave.setOnClickListener(v1 -> {
            saveNote();
        });

        if (!noteInitialized) {
            initNoteFromIntent();
        }
    }

    private void initNoteFromIntent() {
        Note note = getIntent().getParcelableExtra(EXTRA_NOTE);
        if (note != null) {
            edtTitle.setText(note.getTitle());
            edtContent.setText(note.getContent());
            noteForEdit = note;
        } else {
            noteForEdit = null;
        }

        noteInitialized = true;
    }

    private boolean checkIsEditMode() {
        return noteForEdit != null;
    }

    private boolean validateForm() {
        if (edtTitle.getText().toString().isEmpty()) {
            edtTitle.setError("Title is required");
            return false;
        }

        if (edtContent.getText().toString().isEmpty()) {
            edtContent.setError("Content is required");
            return false;
        }

        return true;
    }

    private void saveNote() {
        if (validateForm()) {
            if (!checkIsEditMode()) {
                createNote();
            } else {
                updateNote();
            }
        }
    }

    private void createNote() {
        String title = edtTitle.getText().toString();
        String content = edtContent.getText().toString();
        boolean result = createUpdateViewModel.createNote(title, content);

        Intent intent = new Intent(CreateUpdateNoteActivity.this, MainActivity.class);
        intent.putExtra(RESULT_TAG, result);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateNote() {
        String title = edtTitle.getText().toString();
        String content = edtContent.getText().toString();
        boolean result = createUpdateViewModel.updateNote(noteForEdit.getId(), title, content);

        Intent intent = new Intent(CreateUpdateNoteActivity.this, MainActivity.class);
        intent.putExtra(RESULT_TAG, result);
        setResult(RESULT_OK, intent);
        finish();
    }

    void deleteNote() {
        boolean result = createUpdateViewModel.deleteNote(noteForEdit.getId());
        Intent intent = new Intent(CreateUpdateNoteActivity.this, MainActivity.class);
        intent.putExtra(RESULT_TAG, result);
        setResult(RESULT_OK, intent);
        finish();
    }

    void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the message show for the Alert time
        builder.setMessage(getResources().getString(R.string.delete_note_message));

        // Set Alert Title
        builder.setTitle(getResources().getString(R.string.warning_title));

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton(getResources().getString(R.string.yes), (DialogInterface.OnClickListener) (dialog, which) -> {
            deleteNote();
        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton(getResources().getString(R.string.no), (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }
}