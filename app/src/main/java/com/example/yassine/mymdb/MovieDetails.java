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
import com.example.yassine.mymdb.api.ApiService;
import com.example.yassine.mymdb.api.Client;
import com.example.yassine.mymdb.models.DatabaseHelper;
import com.example.yassine.mymdb.models.Movie;
import com.example.yassine.mymdb.models.Trailer;
import com.example.yassine.mymdb.models.TrailerResponse;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.makeText;

public class MovieDetails extends YouTubeBaseActivity {

    private Movie movie;
    private String language;
    private boolean isEnable = false;
    ImageButton ButtonStar;
    ImageButton ButtonShare;
    DatabaseHelper myDb;
    Toast toast;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    private ApiService movieService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getLayoutInflater().inflate(R.layout.activity_movie_details, frameLayout);
//        setTitle(getString(R.string.movie_details));
        super.setContentView(R.layout.activity_movie_details);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        language = pref.getString("lang", null);

        movieService = Client.getClient().create(ApiService.class);

        myDb = new DatabaseHelper(this);


        Intent i = getIntent();
        movie = (Movie) i.getSerializableExtra("movie");
        ButtonStar = (ImageButton) findViewById(R.id.favorite);
        ButtonShare = (ImageButton) findViewById(R.id.share);

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
                    toast = Toast.makeText(MovieDetails.this,getString(R.string.deleted_from_favs),Toast.LENGTH_LONG);
                    ButtonStar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
                }else{
                    boolean isInserted = myDb.insertData(movie.getId().toString(), movie.getTitle(), movie.getOverview(),
                            movie.posterPath, movie.backdropPath, movie.getVoteAverage());
                    if(isInserted) {
                        if (toast!=null)
                            toast.cancel();
                        toast = makeText(MovieDetails.this, getString(R.string.added_to_favs), Toast.LENGTH_LONG);
                    } else {
                        return;
                    }

                    ButtonStar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
                }

                toast.show();
                isEnable = !isEnable;
            }
        });


        ButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = getString(R.string.share_must_watch) + " " + movie.getTitle();
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_window_title)));


            }
        });


        Glide.with(this)
                .load(poster)
                .into(thumbnail);

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);


        callgetTrailerApi().enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                final List<Trailer> results = response.body().getResults();

                if (results != null && results.size()>0) {
                    onInitializedListener = new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                            youTubePlayer.cueVideo(results.get(0).getKey());
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    };

                    youTubePlayerView.initialize(getString(R.string.youtube_api_key), onInitializedListener);
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });






    }




    public void loadIcon() {
        if (isEnable) {
            ButtonStar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
        } else {
            ButtonStar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
        }
    }


    private Call<TrailerResponse> callgetTrailerApi() {
        return movieService.getTrailer(movie.getId(),
                getString(R.string.api_key),
                language);
    }




}
