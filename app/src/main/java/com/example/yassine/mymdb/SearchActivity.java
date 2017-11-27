package com.example.yassine.mymdb;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.yassine.mymdb.Utils.SimpleDividerItemDecoration;
import com.example.yassine.mymdb.api.ApiService;
import com.example.yassine.mymdb.api.Client;
import com.example.yassine.mymdb.models.Movie;
import com.example.yassine.mymdb.models.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseDrawerActivity {

    private RecyclerView rv;
    private ApiService movieService;
    private String language = "fr-FR";
    private Button btnSearch;
    private EditText searchInput;
    private Context context;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_search, frameLayout);
        setTitle(getString(R.string.search));
        movieService = Client.getClient().create(ApiService.class);
        context = this;
        activity = SearchActivity.this;

        btnSearch = (Button) findViewById(R.id.btnSearch);
        searchInput = (EditText) findViewById(R.id.searchInput);
        rv = (RecyclerView) findViewById(R.id.searchRecycler);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        language = pref.getString("lang", null);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setLayoutManager(linearLayoutManager);

        List<Movie> results = new ArrayList<>();
        FavoritesAdapter adapter = new FavoritesAdapter(context, results);
        rv.setAdapter(adapter);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchInput.getText() != null && searchInput.getText().toString().length() > 0) {
                    hideSoftKeyboard(activity);
                    SearchMovie(searchInput.getText().toString());
                }

            }
        });
    }


    private void SearchMovie(String query) {

        callSearchMovieApi(query).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                List<Movie> results = response.body().getResults();
                FavoritesAdapter adapter = new FavoritesAdapter(context, results);
                rv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }


    private Call<MoviesResponse> callSearchMovieApi(String query) {
        return movieService.searchMovie(
                query,
                getString(R.string.api_key),
                language,
                1
        );
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}
