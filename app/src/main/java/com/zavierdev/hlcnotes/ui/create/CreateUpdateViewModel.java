package com.zavierdev.hlcnotes.ui.create;

import androidx.lifecycle.ViewModel;

import com.zavierdev.hlcnotes.db.DatabaseManager;
import com.zavierdev.hlcnotes.di.DatabaseInjector;

public class CreateUpdateViewModel extends ViewModel {
    public final DatabaseManager databaseManager = DatabaseInjector.getDatabase();

    public boolean createNote(String title, String content) {
        long id = databaseManager.insert(title, content);
        return id != -1;
    }

    public boolean updateNote(long id, String title, String content) {
        int rows = databaseManager.update(id, title, content);
        return rows > 0;
    }

    public boolean deleteNote(long id) {
        int rows = databaseManager.delete(id);
        return rows > 0;
    }
}
