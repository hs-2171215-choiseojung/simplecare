package com.cookandroid.simplecare;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;

    public TodoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 데이터베이스 테이블 생성 쿼리
        String createTableQuery = "CREATE TABLE " + TodoContract.TodoEntry.TABLE_NAME + " (" +
                TodoContract.TodoEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TodoContract.TodoEntry.COLUMN_TITLE + " TEXT, " +
                TodoContract.TodoEntry.COLUMN_DATE + " TEXT)";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스 업그레이드 시 처리할 작업 (테이블 변경 등)
        String dropTableQuery = "DROP TABLE IF EXISTS " + TodoContract.TodoEntry.TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }
}
