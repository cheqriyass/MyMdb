package com.example.yassine.mymdb;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.yassine.mymdb.Utils.SimpleDividerItemDecoration;
import com.example.yassine.mymdb.models.DatabaseHelper;
import com.example.yassine.mymdb.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavoriesActivity extends BaseDrawerActivity {

    private DatabaseHelper myDb;
    private RecyclerView rv;
    private List<Movie> movies = new ArrayList<>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_favories, frameLayout);
        setTitle(getString(R.string.favorites));
        context = this;
        myDb = new DatabaseHelper(this);
        Cursor res = myDb.getAllData();
        rv = (RecyclerView) findViewById(R.id.favoritesRecycler);


        while (res.moveToNext()) {
            Log.v("movie", res.getString(0) + " " + res.getString(1));
            Movie m = new Movie(Integer.parseInt(res.getString(0)), res.getString(1),
                    res.getString(3), res.getString(2), res.getString(4), Double.parseDouble(res.getString(5)));
            movies.add(m);
        }

        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        FavoritesAdapter adapter = new FavoritesAdapter(this, movies);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
    }

}
