package com.markfeldman.popularmovies.data_helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.Time;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class JSONParser {
    public JSONParser(){

    }

    public String[] getMovieImagesURL(String moviesJsonString) throws JSONException {
        final String MOVIEDB_RESULTS = "results";
        final String POSTER_PATH = "poster_path";

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray results =  jsonObject.getJSONArray(MOVIEDB_RESULTS);

        String[] imageURL = new String[results.length()];

        for (int i = 0; i<imageURL.length;i++){
            JSONObject individualMovie = results.getJSONObject(i);
            String moviePosterTag = individualMovie.getString(POSTER_PATH);
            imageURL[i] = moviePosterTag;
        }

        return imageURL;
    }

}
