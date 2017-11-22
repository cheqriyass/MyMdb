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

    public static int layout = 0;
    private static final String TAG = "MainActivity";
    private static String language = "fr_FR";

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.movies_list, frameLayout);
        setTitle(getString(R.string.movies));


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        language = pref.getString("lang", null);

        layout = 0;
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);



        movieService = Client.getClient().create(ApiService.class);
        rv = (RecyclerView) findViewById(R.id.moviesRecycler);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);

        adapter = new PaginationAdapterMovies(this);
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        gridLayoutManager = new GridLayoutManager(this, 3);

        rv.setItemAnimator(new DefaultItemAnimator());


        rv.setLayoutManager(linearLayoutManager);
        setScroll();
        rv.setAdapter(adapter);


        loadFirstPage();



    }

    @Override
    protected void onResume() {
        super.onResume();
        // to check current activity in the navigation drawer
        navigationView.getMenu().getItem(0).setChecked(true);
    }



    //***********************************************************************************

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }


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
            if (layout == 2) {
                rv.setLayoutManager(gridLayoutManager);
            }
            else {
                rv.setLayoutManager(linearLayoutManager);
            }
            setScroll();
            rv.setAdapter(adapter);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.movies) {
//            Intent intent = new Intent(this, MoviesActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.tvshows) {
//            Intent intent = new Intent(this, SeriesActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.about) {
//            Intent intent = new Intent(this, AboutActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.settings) {
//            Intent intent = new Intent(this, SettingsActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.favories) {
//            Intent intent = new Intent(this, FavoriesActivity.class);
//            startActivity(intent);
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }


    //**********************************************************************************

    private void
    loadFirstPage() {
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


    void setScroll(){
        LinearLayoutManager layoutManager= null;

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