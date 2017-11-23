package com.example.yassine.mymdb.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Client {
    private static final String API_URL = "http://api.themoviedb.org/3/";
    private static Retrofit retrofit;

    public static Retrofit getClient(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(
                            GsonConverterFactory.create()
                    ).build();
        }

        return retrofit;
    }
}
