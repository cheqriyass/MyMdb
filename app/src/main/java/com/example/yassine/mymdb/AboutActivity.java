package com.example.yassine.mymdb;

import android.os.Bundle;

public class AboutActivity extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_about, frameLayout);
        setTitle(getString(R.string.movies));
    }
}
