package com.zavierdev.hlcnotes.ui.main;

import android.database.Cursor;

import androidx.lifecycle.ViewModel;

import com.zavierdev.hlcnotes.db.DatabaseHelper;
import com.zavierdev.hlcnotes.db.DatabaseManager;
import com.zavierdev.hlcnotes.di.DatabaseInjector;
import com.zavierdev.hlcnotes.model.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivityViewModel extends ViewModel {
    public final DatabaseManager databaseManager = DatabaseInjector.getDatabase();

    public List<Note> getNotes() {
        Cursor cursor = databaseManager.fetch();
        List<Note> noteList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper._ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTENT));
                String dateString = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
                long timestamp = Long.parseLong(dateString);
                Date date = new Date(timestamp);
                noteList.add(new Note(id, title, content, date));
            } while (cursor.moveToNext());
        }

        return noteList;
    }
}
