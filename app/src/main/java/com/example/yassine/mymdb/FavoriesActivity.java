package com.example.yassine.mymdb;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.yassine.mymdb.models.DatabaseHelper;

public class FavoriesActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    private String language = "fr_FR";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favories);
        myDb = new DatabaseHelper(this);
        Cursor res = myDb.getAllData();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        language = pref.getString("lang", null);

        while(res.moveToNext()){

        }

    }

}
