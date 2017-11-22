package com.example.yassine.mymdb.api;


import com.example.yassine.mymdb.models.MoviesResponse;
import com.example.yassine.mymdb.models.SeriesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Yassine on 15/11/2017.
 */

public interface ApiService {
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey,
                                          @Query("language") String language,
                                          @Query("page") int pageIndex);
    @GET("tv/popular")
    Call<SeriesResponse> getPopularSeries(@Query("api_key") String apiKey,
                                          @Query("language") String language,
                                          @Query("page") int pageIndex);
}
