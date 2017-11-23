package com.example.yassine.mymdb.models;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Serie implements Serializable {
    @SerializedName("id")
    private Integer id;

    @SerializedName("original_name")
    private String title;

    @SerializedName("popularity")
    private Double popularity;

    @SerializedName("poster_path")
    public String posterPath;

    @SerializedName("vote_average")
    public double voteAverage;

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

    private String getQuality(Context context) {
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        String quality = pref.getString("quality", null);
        if (quality == null)
            return "w300";
        return quality;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Serie(Integer id, String title, String posterPath, String overview, String backdropPath) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.backdropPath = backdropPath;
    }

    public Serie() {
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }
}
