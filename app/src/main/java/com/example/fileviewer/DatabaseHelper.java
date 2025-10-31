package com.example.fileviewer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "education.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_GOSTS = "gosts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NUMBER = "number";
    private static final String COLUMN_CODE = "code";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_GOSTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NUMBER + " TEXT NOT NULL, " +
                COLUMN_CODE + " TEXT NOT NULL, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_DESCRIPTION + " TEXT" +
                ")";
        db.execSQL(createTable);
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOSTS);
        onCreate(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        List<String[]> gosts = new ArrayList<String[]>();
        gosts.add(new String[]{"00", "ГОСТ Р 57723-2017", "Информационные технологии", "Системы образовательные электронные. Общие положения"});
        gosts.add(new String[]{"01", "ГОСТ Р 53624-2009", "Информационные технологии", "Информационно-коммуникационные технологии в образовании"});
        gosts.add(new String[]{"02", "ГОСТ Р 52653-2006", "Информационные технологии", "Образовательные электронные издания и ресурсы"});
        gosts.add(new String[]{"03", "ГОСТ Р 52656-2006", "Системы образовательные", "Требования к качеству электронных образовательных ресурсов"});
        gosts.add(new String[]{"04", "ГОСТ Р 57724-2017", "Технологии обучения", "Системы управления обучением. Функциональные требования"});
        gosts.add(new String[]{"05", "ГОСТ Р 53625-2009", "Образовательные платформы", "Требования к системам дистанционного обучения"});
        gosts.add(new String[]{"06", "ГОСТ Р 52657-2006", "Оценка качества", "Методы оценки эффективности образовательных технологии"});
        gosts.add(new String[]{"07", "ГОСТ Р 57725-2017", "Искусственный интеллект", "Применение ИИ в адаптивных образовательных системах"});

        for (String[] gost : gosts) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NUMBER, gost[0]);
            values.put(COLUMN_CODE, gost[1]);
            values.put(COLUMN_TITLE, gost[2]);
            values.put(COLUMN_DESCRIPTION, gost[3]);
            db.insert(TABLE_GOSTS, null, values);
        }
    }
}