package com.example.yassine.mymdb;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

import static com.example.yassine.mymdb.R.id.spinner1;

public class SettingsActivity extends BaseDrawerActivity {

    private Spinner spinner;
    private boolean firstLaunch = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_settings, frameLayout);
        setTitle(getString(R.string.settings));

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        String language = pref.getString("lang", null);



        spinner = (Spinner) findViewById(spinner1);


        if (language=="fr-FR")
            spinner.setSelection(0);
        else
            spinner.setSelection(1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();


                String lang = parentView.getItemAtPosition(position).toString();
                Locale locale;
                Configuration config;

                switch (lang){
                    case "French":
                    case "Francais":
                        editor.putString("lang", "fr-FR").apply();
//                        locale = new Locale("fr-FR");
//                        Locale.setDefault(locale);
                        break;
                    default:
                        editor.putString("lang", "en-US").apply();
//                        locale = new Locale("en-US");
//                        Locale.setDefault(locale);
                }

//                config = new Configuration();
//                config.locale = locale;
//                Resources resources = getResources();
//                resources.updateConfiguration(config, resources.getDisplayMetrics());

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




    }


//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(this, MoviesActivity.class));
//    }
//

}
