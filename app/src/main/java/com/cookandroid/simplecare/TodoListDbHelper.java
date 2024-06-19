package com.cookandroid.simplecare;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoListDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todo_list.db";
    private static final int DATABASE_VERSION = 1;

    public TodoListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TodoContract.TodoEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TodoContract.TodoEntry.DELETE_TABLE);
        onCreate(db);
    }
}
