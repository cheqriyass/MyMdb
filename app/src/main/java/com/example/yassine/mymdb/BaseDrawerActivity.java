package com.example.yassine.mymdb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class BaseDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static int layout = 0;
    public static int oldLayout = 0;
    DrawerLayout drawerLayout;
    FrameLayout frameLayout;
    NavigationView navigationView;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setLocal();
        super.setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.isChecked()) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }


        Intent intent;
        if (id == R.id.movies) {
            layout = oldLayout;
            intent = new Intent(this, MoviesActivity.class);
        } else if (id == R.id.tvshows) {
            layout = oldLayout;
            intent = new Intent(this, SeriesActivity.class);
        } else if (id == R.id.about) {
            intent = new Intent(this, AboutActivity.class);
        } else if (id == R.id.settings) {
            intent = new Intent(this, SettingsActivity.class);
        } else if (id == R.id.search) {
            layout = 0;
            intent = new Intent(this, SearchActivity.class);
        } else { // favorites
            layout = 0;
            intent = new Intent(this, FavoriesActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setLocal() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        String language = pref.getString("lang", null);
        if (language == null) {
            language = "fr-FR";
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("lang", language).apply();
        }
        Locale locale;

        switch (language) {
            case "fr-FR":
                locale = new Locale("fr");
                break;
            default:
                locale = new Locale("en");
        }


        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}
