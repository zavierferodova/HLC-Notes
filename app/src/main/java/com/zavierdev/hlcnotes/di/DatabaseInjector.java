package com.zavierdev.hlcnotes.di;

import android.content.Context;

import com.zavierdev.hlcnotes.db.DatabaseManager;

public class DatabaseInjector {
    private static DatabaseManager database = null;
    private static Context context = null;

    public static void init(Context context) {
        DatabaseInjector.context = context;
    }

    public static synchronized DatabaseManager getDatabase() {
        if (context != null) {
            if (database == null) {
                database = new DatabaseManager(context);
                database = database.open();
            }
        }

        return database;
    }
}
