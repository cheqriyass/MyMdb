package com.example.yassine.mymdb.models;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Movie implements Serializable {
    @SerializedName("id")
    private Integer id;

    @SerializedName("title")
    private String title;

    @SerializedName("popularity")
    private Double popularity;

    @SerializedName("poster_path")
    public String posterPath;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("overview")
    private String overview;

    @SerializedName("backdrop_path")
    public String backdropPath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath(Context context) {

        return "https://image.tmdb.org/t/p/" + getQuality(context) + posterPath;
    }

    private String getQuality(Context context) {
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        String quality = pref.getString("quality", null);
        if (quality == null)
            return "w300";
        return quality;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBackdropPath(Context context) {
        return "https://image.tmdb.org/t/p/" + getQuality(context) + backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Movie(Integer id, String title, String posterPath, String overview, String backdropPath, double vote) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.backdropPath = backdropPath;
        this.voteAverage = vote;
    }

    public Movie() {
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }
}
