package com.example.yassine.mymdb;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.yassine.mymdb.Utils.PaginationScrollListener;
import com.example.yassine.mymdb.api.ApiService;
import com.example.yassine.mymdb.api.Client;
import com.example.yassine.mymdb.models.Movie;
import com.example.yassine.mymdb.models.MoviesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActiviy extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static int layout = 0;
    private static final String TAG = "MainActivity";
    private static String language = "fr_FR";

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private int TOTAL_PAGES = 10;
    private int currentPage = PAGE_START;

    private ApiService movieService;



    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_list);

        rv = (RecyclerView) findViewById(R.id.moviesRecycler);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);

        adapter = new PaginationAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        gridLayoutManager = new GridLayoutManager(this, 3);

        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());


        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        movieService = Client.getClient().create(ApiService.class);

        loadFirstPage();

    }


    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        callPopularMoviesApi().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                // Got data. Send it to adapter

                List<Movie> results = response.body().getResults();
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void loadNextPage() {

        callPopularMoviesApi().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                List<Movie> results = response.body().getResults();
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }



    private Call<MoviesResponse> callPopularMoviesApi() {
        return movieService.getPopularMovies(
                getString(R.string.api_key),
                language,
                currentPage
        );
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.change_layout) {
            layout = ++layout%3;
//            rv.setLayoutManager(linearLayoutManager);
            if (layout == 2) {
                setContentView(R.layout.movies_list_grid);
                rv.setLayoutManager(gridLayoutManager);

            }
            else {
//                setContentView(R.layout.movies_list);
                rv.setLayoutManager(linearLayoutManager);
            }
            rv.setAdapter(adapter);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}




//    private void LoadMovies() {
//
//        try {
//            Client client = new Client();
//            ApiService api = client.getClient().create(ApiService.class);
//
//            Call<MoviesResponse> call = api.getPopularMovies(apiKey, "fr_FR", 1);
//
//            call.enqueue(new Callback<MoviesResponse>() {
//                @Override
//                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
//                    List<Movie> movies = response.body().getResults();
//                    recyclerView.setLayoutManager(new LinearLayoutManager(retroActivity));
//                    recyclerView.setHasFixedSize(true);
//                    recyclerView.setItemAnimator(new DefaultItemAnimator());
//                    movieAdapter = new MovieAdapter(context, movies);
//                    recyclerView.setAdapter(movieAdapter);
//                }
//
//                @Override
//                public void onFailure(Call<MoviesResponse> call, Throwable t) {
//                    Log.d("ERROR", t.getMessage());
//                    Toast.makeText(MovieListActiviy.this, "oups", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }