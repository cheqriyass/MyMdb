package com.example.yassine.mymdb;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.yassine.mymdb.models.Serie;

import static android.widget.Toast.makeText;

public class SerieDetailsActivity extends AppCompatActivity {

    private Serie movie;
    private String language;
    private boolean isEnable = false;
    ImageButton ButtonStar;
    DatabaseHelper myDb;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_details);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        language = pref.getString("lang", null);


        myDb = new DatabaseHelper(this);


        Intent i = getIntent();
        movie = (Serie) i.getSerializableExtra("movie");
        ButtonStar = (ImageButton) findViewById(R.id.favorite);

        TextView movie_title = (TextView) findViewById(R.id.movie_title);
        TextView movie_desc = (TextView) findViewById(R.id.movie_desc);
        ImageView thumbnail = (ImageView) findViewById(R.id.thumbnail);

        movie_title.setText(movie.getTitle());
        movie_desc.setText(movie.getOverview());

        final String poster = movie.getBackdropPath();


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
                    toast = Toast.makeText(SerieDetailsActivity.this,"Film supprimé des favories",Toast.LENGTH_LONG);
                    ButtonStar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
                }else{
                    boolean isInserted = myDb.insertData(movie.getId().toString(), movie.getTitle().toString(), movie.getOverview().toString(),
                            movie.posterPath, movie.backdropPath);
                    if(isInserted == true) {
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

