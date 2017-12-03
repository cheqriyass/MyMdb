package com.example.yassine.mymdb.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favories.db";
    private static final String TABLE_NAME = "favories_table";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TITLE";
    private static final String COL_3 = "DESC";
    private static final String COL_4 = "POSTER";
    private static final String COL_5 = "POSTERLARGE";
    private static final String COL_6 = "VOTE_AVERAGE";
    private static final String COL_7 = "IS_MOVIE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY, TITLE TEXT, DESC TEXT," +
                " POSTER TEXT, POSTERLARGE TEXT, VOTE_AVERAGE, IS_MOVIE INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String id, String title, String description, String poster, String posterLarge, double vote, int isMovie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, title);
        contentValues.put(COL_3, description);
        contentValues.put(COL_4, poster);
        contentValues.put(COL_5, posterLarge);
        contentValues.put(COL_6, vote);
        contentValues.put(COL_7, isMovie);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;

    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select ID, TITLE, DESC, POSTER, POSTERLARGE, VOTE_AVERAGE, IS_MOVIE from " + TABLE_NAME, null);
    }


    public Cursor getById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select ID, TITLE, DESC, POSTER, POSTERLARGE, VOTE_AVERAGE, IS_MOVIE from " + TABLE_NAME + " where ID=" + id, null);
    }


    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }
}