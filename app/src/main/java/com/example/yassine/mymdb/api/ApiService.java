package com.example.yassine.mymdb.api;


import com.example.yassine.mymdb.models.MoviesResponse;
import com.example.yassine.mymdb.models.SeriesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey,
                                          @Query("language") String language,
                                          @Query("page") int pageIndex);

    @GET("tv/popular")
    Call<SeriesResponse> getPopularSeries(@Query("api_key") String apiKey,
                                          @Query("language") String language,
                                          @Query("page") int pageIndex);

    @GET("search/movie")
    Call<MoviesResponse> searchMovie(@Query("query") String query,
                                     @Query("api_key") String apiKey,
                                     @Query("language") String language,
                                     @Query("page") int pageIndex);
}
