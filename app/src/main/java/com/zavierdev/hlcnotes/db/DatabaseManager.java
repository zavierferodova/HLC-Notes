package com.zavierdev.hlcnotes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        this.context = context;
    }

    public DatabaseManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(String title, String content) {
        Date date = new Date();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_CONTENT, content);
        values.put(DatabaseHelper.COLUMN_DATE, date.getTime());
        return database.insert(DatabaseHelper.TABLE_NAME, null, values);
    }

    public Cursor fetch() {
        String[] columns = new String[]{
                DatabaseHelper._ID,
                DatabaseHelper.COLUMN_TITLE,
                DatabaseHelper.COLUMN_CONTENT,
                DatabaseHelper.COLUMN_DATE
        };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, DatabaseHelper.COLUMN_DATE + " DESC");
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String title, String content) {
        Date date = new Date();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_CONTENT, content);
        values.put(DatabaseHelper.COLUMN_DATE, date.getTime());
        int i = database.update(DatabaseHelper.TABLE_NAME, values,
                DatabaseHelper._ID + " = " + _id,
                null);
        return i;
    }

    public int delete(long _id) {
        int i = database.delete(DatabaseHelper.TABLE_NAME,
                DatabaseHelper._ID + " = " + _id,
                null);
        return i;
    }
}
