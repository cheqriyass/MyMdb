package com.example.yassine.mymdb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yassine.mymdb.models.DatabaseHelper;
import com.example.yassine.mymdb.models.Serie;

import static android.widget.Toast.makeText;

public class SerieDetailsActivity extends BaseDrawerActivity {

    private Serie movie;
    private String language;
    private boolean isEnable = false;
    ImageButton ButtonStar;
    DatabaseHelper myDb;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_movie_details, frameLayout);
        setTitle(getString(R.string.movie_details));

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        language = pref.getString("lang", null);


        myDb = new DatabaseHelper(this);


        Intent i = getIntent();
        movie = (Serie) i.getSerializableExtra("movie");
        ButtonStar = (ImageButton) findViewById(R.id.favorite);

        TextView movie_title = (TextView) findViewById(R.id.movie_title);
        TextView movie_desc = (TextView) findViewById(R.id.movie_desc);
        TextView rating = (TextView) findViewById(R.id.rating);
        ImageView thumbnail = (ImageView) findViewById(R.id.thumbnail);

        movie_title.setText(movie.getTitle());
        movie_desc.setText(movie.getOverview());
        rating.setText("Rating: " + Double.toString(movie.getVoteAverage()));

        final String poster = movie.getBackdropPath(this);


        Cursor res = myDb.getById(movie.getId().toString());
        isEnable = res.getCount() != 0;

        loadIcon();

        ButtonStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEnable){

                    Integer deletedRows = myDb.deleteData(movie.getId().toString());
                    if(deletedRows > 0)
                        if (toast!=null)
                            toast.cancel();
                    toast = Toast.makeText(SerieDetailsActivity.this,"Film supprimé des favories",Toast.LENGTH_LONG);
                    ButtonStar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
                }else{
                    boolean isInserted = myDb.insertData(movie.getId().toString(), movie.getTitle(), movie.getOverview(),
                            movie.posterPath, movie.backdropPath, movie.getVoteAverage());
                    if(isInserted) {
                        if (toast!=null)
                            toast.cancel();
                        toast = makeText(SerieDetailsActivity.this, "Film ajouté au favories", Toast.LENGTH_LONG);
                    } else {
                        if (toast!=null)
                            toast.cancel();
                        toast = makeText(SerieDetailsActivity.this, "Error", Toast.LENGTH_LONG);
                        return;
                    }

                    ButtonStar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
                }

                toast.show();
                isEnable = !isEnable;
            }
        });


        Glide.with(this)
                .load(poster)
                .into(thumbnail);

    }


    public void loadIcon() {
        if (isEnable) {
            ButtonStar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
        } else {
            ButtonStar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
        }
    }




}

