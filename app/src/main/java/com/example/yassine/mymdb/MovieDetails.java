package com.example.yassine.mymdb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yassine.mymdb.models.Movie;

public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent i = getIntent();
        Movie movie = (Movie) i.getSerializableExtra("movie");

        TextView movie_title = (TextView) findViewById(R.id.movie_title);;
        TextView movie_desc = (TextView) findViewById(R.id.movie_desc);
        ImageView thumbnail = (ImageView) findViewById(R.id.thumbnail);

        movie_title.setText(movie.getTitle());
        movie_desc.setText("Description: " + movie.getOverview());

        String poster = "https://image.tmdb.org/t/p/w500" + movie.getBackdropPath();


        Glide.with(this)
                .load(poster)
                //.placeholder(R.drawable.load)
                .into(thumbnail);

    }
}
