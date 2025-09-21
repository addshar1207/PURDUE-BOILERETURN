package com.example.boilereport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "boilereport.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_REPORTS = "reports";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FINDER_NAME = "finder_name";
    public static final String COLUMN_FINDER_PHONE = "finder_phone";
    public static final String COLUMN_FINDER_EMAIL = "finder_email";
    public static final String COLUMN_ITEM_TYPE = "item_type";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_BUILDING = "building";
    public static final String COLUMN_DESCRIPTION = "description";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REPORTS_TABLE = "CREATE TABLE " + TABLE_REPORTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FINDER_NAME + " TEXT, "
                + COLUMN_FINDER_PHONE + " TEXT, "
                + COLUMN_FINDER_EMAIL + " TEXT, "
                + COLUMN_ITEM_TYPE + " TEXT, "
                + COLUMN_ITEM_NAME + " TEXT, "
                + COLUMN_BUILDING + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT"
                + ")";
        db.execSQL(CREATE_REPORTS_TABLE);
    }

    public Cursor getTopLocations(int limit) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT " + COLUMN_BUILDING + ", COUNT(*) as count " +
                        "FROM " + TABLE_REPORTS + " " +
                        "GROUP BY " + COLUMN_BUILDING + " " +
                        "ORDER BY count DESC " +
                        "LIMIT ?",
                new String[]{String.valueOf(limit)}
        );
    }

    public Cursor getFilteredReports(String itemType, String building) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_REPORTS,
                null,
                COLUMN_ITEM_TYPE + "=? AND " + COLUMN_BUILDING + "=?",
                new String[]{itemType, building},
                null, null, null);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTS);
        onCreate(db);
    }

    // Insert a report
    public boolean insertReport(String finderName, String phone, String email, String itemType,
                                String itemName, String building, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FINDER_NAME, finderName);
        values.put(COLUMN_FINDER_PHONE, phone);
        values.put(COLUMN_FINDER_EMAIL, email);
        values.put(COLUMN_ITEM_TYPE, itemType);
        values.put(COLUMN_ITEM_NAME, itemName);
        values.put(COLUMN_BUILDING, building);
        values.put(COLUMN_DESCRIPTION, description);

        long result = db.insert(TABLE_REPORTS, null, values);
        db.close();
        return result != -1;
    }
}
