package com.example.yassine.mymdb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.yassine.mymdb.Utils.SimpleDividerItemDecoration;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesActivity extends BaseDrawerActivity{

    
    private static final String TAG = "MainActivity";
    private static String language;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private int TOTAL_PAGES = 10;
    private int currentPage = PAGE_START;

    private ApiService movieService;



    PaginationAdapterMovies adapter;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;
    RecyclerView.ItemDecoration separator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_movies, frameLayout);
        setTitle(getString(R.string.movies));

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        language = pref.getString("lang", null);

        movieService = Client.getClient().create(ApiService.class);
        rv = (RecyclerView) findViewById(R.id.moviesRecycler);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        separator = new SimpleDividerItemDecoration(this);
        adapter = new PaginationAdapterMovies(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        gridLayoutManager = new GridLayoutManager(this, 3);

        rv.setItemAnimator(new DefaultItemAnimator());

        setView();
        loadFirstPage();
    }

    private void setView() {
        if (layout == 2) {
            rv.removeItemDecoration(separator);
            rv.setLayoutManager(gridLayoutManager);
        }
        else if (layout == 1){
            rv.setLayoutManager(linearLayoutManager);
        } else {
            rv.addItemDecoration(separator);
            rv.setLayoutManager(linearLayoutManager);
        }
        setScroll();
        rv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(0).setChecked(true);
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
            oldLayout = layout;
            setView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //**********************************************************************************

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");


        callPopularMoviesApi().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
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


    void setScroll(){
        LinearLayoutManager layoutManager;

        if (layout == 2)
            layoutManager = gridLayoutManager;
        else
            layoutManager = linearLayoutManager;

        rv.addOnScrollListener(new PaginationScrollListener(layoutManager) {
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
    }


}
