package com.example.yassine.mymdb;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yassine.mymdb.models.DatabaseHelper;
import com.example.yassine.mymdb.models.Movie;

import static android.widget.Toast.makeText;

public class MovieDetails extends AppCompatActivity {

    private Movie movie;
    private boolean isEnable = false;
    ImageButton ButtonStar;
    DatabaseHelper myDb;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        myDb = new DatabaseHelper(this);
        Intent i = getIntent();
        movie = (Movie) i.getSerializableExtra("movie");
        ButtonStar = (ImageButton) findViewById(R.id.favorite);

        TextView movie_title = (TextView) findViewById(R.id.movie_title);;
        TextView movie_desc = (TextView) findViewById(R.id.movie_desc);
        ImageView thumbnail = (ImageView) findViewById(R.id.thumbnail);

        movie_title.setText(movie.getTitle());
        movie_desc.setText("Description: " + movie.getOverview());

        String poster = "https://image.tmdb.org/t/p/w500" + movie.getBackdropPath();


        Cursor res = myDb.getById(movie.getId().toString());
        if(res.getCount() != 0) {
            isEnable = true;
        }else {
            isEnable = false;
        }

        loadIcon();

        ButtonStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEnable){

                    Integer deletedRows = myDb.deleteData(movie.getId().toString());
                    if(deletedRows > 0)
                        if (toast!=null)
                            toast.cancel();
                    toast = Toast.makeText(MovieDetails.this,"Film supprimé des favories",Toast.LENGTH_LONG);
                    ButtonStar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
                }else{
                    boolean isInserted = myDb.insertData(movie.getId().toString());
                    if(isInserted == true) {
                        if (toast!=null)
                            toast.cancel();
                        toast = makeText(MovieDetails.this, "Film ajouté au favories", Toast.LENGTH_LONG);
                    }

                    ButtonStar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
                }

                toast.show();
                isEnable = !isEnable;
            }
        });


        Glide.with(this)
                .load(poster)
                //.placeholder(R.drawable.load)
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
