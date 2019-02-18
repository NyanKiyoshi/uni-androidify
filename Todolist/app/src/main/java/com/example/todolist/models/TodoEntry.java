package com.example.todolist.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.todolist.controllers.DatabaseWrapper;

import java.util.ArrayList;

public final class TodoEntry {
    public static class MetaData implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TEXT = "text";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_TEXT + " TEXT)";
    }

    public long id;
    public final String text;

    public TodoEntry(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public TodoEntry(String text) {
        this(-1, text);
    }

    public static ArrayList<TodoEntry> getEntries(DatabaseWrapper database) {
        String sortOrder =
                MetaData._ID + " ASC";

        Cursor cursor = database.getReadableDatabase().query(
                MetaData.TABLE_NAME,    // The table to query
                null,                   // The array of columns to return (pass null to get all)
                null,                   // The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        ArrayList<TodoEntry> todoEntries = new ArrayList<>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(MetaData._ID));
            String itemText = cursor.getString(
                    cursor.getColumnIndexOrThrow(MetaData.COLUMN_NAME_TEXT));

            todoEntries.add(new TodoEntry(itemId, itemText));
        }

        cursor.close();

        return todoEntries;
    }

    public void createEntry(DatabaseWrapper database) {
        SQLiteDatabase db = database.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MetaData.COLUMN_NAME_TEXT, this.text);

        this.id = db.insert(MetaData.TABLE_NAME, null, values);;
    }

    /**
     * @param database The database wrapper.
     * @return The deleted row count.
     */
    public int deleteEntry(DatabaseWrapper database) {
        SQLiteDatabase db = database.getWritableDatabase();
        String selection = MetaData._ID + " = ?";
        String[] selectionArgs = { this.getIdStr() };

        return db.delete(MetaData.TABLE_NAME, selection, selectionArgs);
    }

    public String getIdStr() {
        return String.valueOf(this.id);
    }
}
