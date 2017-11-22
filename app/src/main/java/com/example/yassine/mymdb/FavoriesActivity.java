package com.example.yassine.mymdb;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.yassine.mymdb.models.DatabaseHelper;

public class FavoriesActivity extends AppCompatActivity {

    private DatabaseHelper myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favories);
        myDb = new DatabaseHelper(this);
        Cursor res = myDb.getAllData();


        while(res.moveToNext()){
            Log.v("movie", res.getString(0) + " " + res.getString(1));

        }

    }

}
