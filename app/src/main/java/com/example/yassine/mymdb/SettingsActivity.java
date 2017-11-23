package com.example.yassine.mymdb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends BaseDrawerActivity {

    private Spinner langSpinner;
    private Spinner qualSpinner;
    private boolean firstLaunch = true;
    private boolean firstLaunchQual = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_settings, frameLayout);
        setTitle(getString(R.string.settings));

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        String language = pref.getString("lang", null);
        String qual = pref.getString("quality", null);



        langSpinner = (Spinner) findViewById(R.id.lang_spinner);
        qualSpinner = (Spinner) findViewById(R.id.qual_spinner);


        if (language.equals("fr-FR"))
            langSpinner.setSelection(0);
        else
            langSpinner.setSelection(1);


        if (qual.equals("original"))
            qualSpinner.setSelection(0);
        else if (qual.equals("w780"))
            qualSpinner.setSelection(1);
        else
            qualSpinner.setSelection(2);



        langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();


                String lang = parentView.getItemAtPosition(position).toString();

                switch (lang){
                    case "French":
                    case "Français":
                        editor.putString("lang", "fr-FR").apply();
                        break;
                    default:
                        editor.putString("lang", "en-US").apply();
                }


                if (!firstLaunch)
                     Toast.makeText(SettingsActivity.this,
                            parentView.getItemAtPosition(position).toString(),
                            Toast.LENGTH_SHORT).show();

                firstLaunch = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });


        qualSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();


                String quality = parentView.getItemAtPosition(position).toString();

                editor.putString("quality", quality).apply();



                if (!firstLaunchQual)
                    Toast.makeText(SettingsActivity.this,
                            parentView.getItemAtPosition(position).toString(),
                            Toast.LENGTH_SHORT).show();

                firstLaunchQual = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });



    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(this, MoviesActivity.class));
        }
    }


}
