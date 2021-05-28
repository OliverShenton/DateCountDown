package com.datecountdown;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Date;

import static java.util.Calendar.DATE;
import static java.util.Calendar.YEAR;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final String DATABASE_NAME = "DateCountdown.db";
    private static final int DATABASE_VERSION = 10;

    private static final String TABLE_NAME = "my_Date";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DATE = "date";
    private static final String DATE_DISPLAY = "datedisplay";
    private static final String COLUMN_REPEAT = "repeat";
    private static final String DAYS = "days";
    private static final String TIME = "time";
    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String OCCASION = "occasion";


    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "("+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_REPEAT + " TEXT," +
                COLUMN_DATE + " TEXT," +
                DATE_DISPLAY + " TEXT," +
                DAYS + " INTEGER," +
                TIME + " TEXT," +
                HOUR + " INTEGER," +
                MINUTE + " INTEGER," +
                OCCASION + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int ii) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    void addDateFinish(String title, String date, String datedisplay, String repeat, int days, String time, int hour, int minute, String occasion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_REPEAT, repeat);
        cv.put(DATE_DISPLAY, datedisplay);
        cv.put(DAYS, days);
        cv.put(TIME, time);
        cv.put(HOUR, hour);
        cv.put(MINUTE, minute);
        cv.put(OCCASION, occasion);

        long result = db.insert(TABLE_NAME,null, cv);
        if (result == -1) {
            Toast.makeText(context, "Date Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Date Added", Toast.LENGTH_SHORT).show();

        }
    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME +  " ORDER BY " + COLUMN_DATE + " , " + HOUR + " , " + MINUTE + " , " + COLUMN_TITLE + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String row_id, String title, String date, String datedisplay, String repeat, int days, String time, int hour, int minute, String occasion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DATE, date);
        cv.put(DATE_DISPLAY, datedisplay);
        cv.put(COLUMN_REPEAT, repeat);
        cv.put(DAYS, days);
        cv.put(TIME, time);
        cv.put(HOUR, hour);
        cv.put(MINUTE, minute);
        cv.put(OCCASION, occasion);

        long result = db.update(TABLE_NAME, cv, "id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to Update Date", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Date Updated", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete Date", Toast.LENGTH_SHORT).show();
        } else {
            return;
        }
    }

    void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

}
