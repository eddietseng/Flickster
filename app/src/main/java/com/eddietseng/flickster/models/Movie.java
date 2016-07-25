package com.eddietseng.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by eddietseng on 7/18/16.
 */
public class Movie implements Serializable{

    private int id;
    private String posterPath;
    private String backdropPath;
    private String originalTitle;
    private String overview;
    private String releaseDate;
    double voteAverage;

    public Movie(JSONObject object) throws JSONException{
        this.id = object.getInt("id");
        this.posterPath = object.getString("poster_path");
        this.backdropPath = object.getString("backdrop_path");
        this.originalTitle = object.getString("original_title");
        this.overview = object.getString("overview");
        this.releaseDate = object.getString("release_date");
        this.voteAverage = object.getDouble("vote_average");
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();

        for( int i = 0; i < array.length(); ++i){
            try{
                results.add(new Movie(array.getJSONObject(i)));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public int getId() { return id; }

    public String getPosterPath() {
        //better performance
        return "https://image.tmdb.org/t/p/w342/" + posterPath;
    }

    public String getBackdropPath() {
        return "https://image.tmdb.org/t/p/w780/" + backdropPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        if(overview.length() > 290 )
            return overview.substring(0, 290) + "...";
        return overview;
    }

    public String getOriginalOverview() { return overview; }

    public String getReleaseDate() {
        return "Release Date: " + releaseDate;
    }

    public double getVoteAverage() { return voteAverage; }
}
