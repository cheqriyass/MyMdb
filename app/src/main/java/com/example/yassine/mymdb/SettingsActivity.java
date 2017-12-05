package com.example.yassine.mymdb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

public class SettingsActivity extends BaseDrawerActivity {

    private boolean firstLaunch = true;
    private boolean firstLaunchQual = true;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.settings));
        context = this;
        getLayoutInflater().inflate(R.layout.activity_settings, frameLayout);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        String language = pref.getString("lang", null);
        String qual = pref.getString("quality", null);

        if (qual == null) {
            qual = "w300";
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("quality", qual).apply();
        }

        Spinner langSpinner = (Spinner) findViewById(R.id.lang_spinner);
        Spinner qualSpinner = (Spinner) findViewById(R.id.qual_spinner);


        if (language.equals("fr-FR"))
            langSpinner.setSelection(0);
        else
            langSpinner.setSelection(1);


        switch (qual) {
            case "original":
                qualSpinner.setSelection(0);
                break;
            case "w780":
                qualSpinner.setSelection(1);
                break;
            default:
                qualSpinner.setSelection(2);
                break;
        }



        langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();


                String lang = parentView.getItemAtPosition(position).toString();


                Locale locale;

                switch (lang){
                    case "French":
                    case "Français":
                        editor.putString("lang", "fr-FR").apply();
                        locale = new Locale("fr");
                        break;
                    default:
                        locale = new Locale("en");
                        editor.putString("lang", "en-US").apply();
                }

                Resources res = context.getResources();
                Configuration config = new Configuration(res.getConfiguration());
                config.locale = locale;
                res.updateConfiguration(config, res.getDisplayMetrics());


                if (!firstLaunch) {
                    Toast.makeText(SettingsActivity.this,
                            parentView.getItemAtPosition(position).toString(),
                            Toast.LENGTH_SHORT).show();
                    Intent refresh = new Intent(context, SettingsActivity.class);
                    startActivity(refresh);
                    finish();
                }
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
