package com.cookandroid.simplecare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TodoDataSource {

    private static final String TAG = TodoDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private TodoListDbHelper dbHelper;

    public TodoDataSource(Context context) {
        dbHelper = new TodoListDbHelper(context);
    }

    public void open() throws SQLException {
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            Log.e(TAG, "Error opening the database", e);
            throw e;
        }
    }

    public void close() {
        dbHelper.close();
    }

    public long addTodo(String title, String date) {
        ContentValues values = new ContentValues();
        values.put(TodoContract.TodoEntry.COLUMN_TITLE, title);
        values.put(TodoContract.TodoEntry.COLUMN_DATE, date);

        try {
            long insertedId = database.insertOrThrow(TodoContract.TodoEntry.TABLE_NAME, null, values);
            if (insertedId == -1) {
                Log.e(TAG, "Failed to insert todo into database");
            }
            return insertedId;
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting todo into database", e);
            return -1;
        }
    }

    public void deleteTodo(String title, String date) {
        String selection = TodoContract.TodoEntry.COLUMN_TITLE + " = ? AND " +
                TodoContract.TodoEntry.COLUMN_DATE + " = ?";
        String[] selectionArgs = { title, date };

        try {
            int rowsDeleted = database.delete(TodoContract.TodoEntry.TABLE_NAME, selection, selectionArgs);
            if (rowsDeleted == 0) {
                Log.w(TAG, "No todo deleted for title: " + title + " and date: " + date);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting todo from database", e);
        }
    }

    public List<String> getAllTodos(String date) {
        List<String> todos = new ArrayList<>();
        String[] projection = { TodoContract.TodoEntry.COLUMN_TITLE };
        String selection = TodoContract.TodoEntry.COLUMN_DATE + " = ?";
        String[] selectionArgs = { date };

        Cursor cursor = null;
        try {
            cursor = database.query(
                    TodoContract.TodoEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            while (cursor != null && cursor.moveToNext()) {
                String todo = cursor.getString(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_TITLE));
                todos.add(todo);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error querying todos from database", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return todos;
    }
}
