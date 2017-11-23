package com.example.yassine.mymdb.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "favories.db";
    public static final String TABLE_NAME = "favories_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TITLE";
    public static final String COL_3 = "DESC";
    public static final String COL_4 = "POSTER";
    public static final String COL_5 = "POSTERLARGE";
    public static final String COL_6 = "VOTE_AVERAGE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY, TITLE TEXT, DESC TEXT," +
                " POSTER TEXT, POSTERLARGE TEXT, VOTE_AVERAGE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String id, String title, String description, String poster, String posterLarge, double vote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,title);
        contentValues.put(COL_3,description);
        contentValues.put(COL_4,poster);
        contentValues.put(COL_5,posterLarge);
        contentValues.put(COL_6,vote);
        long result = db.insert(TABLE_NAME, null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select ID, TITLE, DESC, POSTER, POSTERLARGE, VOTE_AVERAGE from " + TABLE_NAME,null);
        return res;
    }


    public Cursor getById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select ID, TITLE, DESC, POSTER, POSTERLARGE, VOTE_AVERAGE from "+TABLE_NAME + " where ID=" + id,null);
        return res;
    }


    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}