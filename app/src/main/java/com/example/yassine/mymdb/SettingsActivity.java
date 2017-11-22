package com.example.yassine.mymdb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import static com.example.yassine.mymdb.R.id.spinner1;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinner = (Spinner) findViewById(spinner1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();

                String lang = parentView.getItemAtPosition(position).toString();

                switch (lang){
                    case "French":
                    case "Francais":
                        editor.putString("lang", "fr-FR").apply();
                        break;
                    default:
                        editor.putString("lang", "en-US").apply();

                }


                Toast.makeText(SettingsActivity.this,
                        parentView.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });




    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MovieListActiviy.class));
    }


}
