package com.markfeldman.popularmovies.utilities;



import android.content.ContentValues;

import com.markfeldman.popularmovies.database.MovieContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONParser {
    public JSONParser(){

    }

    public static ContentValues[] getJsonContentValue(String moviesJsonString) throws JSONException {

        final String MOVIE_DB_RESULTS = "results";

        final String POSTER_PATH = "poster_path";
        final String MOVIE_PLOT = "overview";
        final String MOVIE_RELEASE = "release_date";
        final String MOVIE_ID = "id";
        final String MOVIE_TITLE = "title";
        final String MOVIE_RATING = "vote_average";

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray results =  jsonObject.getJSONArray(MOVIE_DB_RESULTS);
        ContentValues[] cvArray = new ContentValues[results.length()];


        for (int i = 0; i<results.length();i++){
            JSONObject individualMovie = results.getJSONObject(i);
            ContentValues cv = new ContentValues();
            String moviePosterTag = individualMovie.getString(POSTER_PATH);
            String plot = individualMovie.getString(MOVIE_PLOT);
            String release = individualMovie.getString(MOVIE_RELEASE);
            String movID = individualMovie.getString(MOVIE_ID);
            String title = individualMovie.getString(MOVIE_TITLE);
            String rating = individualMovie.getString(MOVIE_RATING);

            cv.put(MovieContract.MovieDataContract.MOVIE_POSTER_TAG, moviePosterTag);
            cv.put(MovieContract.MovieDataContract.MOVIE_PLOT,plot);
            cv.put(MovieContract.MovieDataContract.MOVIE_RELEASE,release);
            cv.put(MovieContract.MovieDataContract.MOVIE_ID,movID);
            cv.put(MovieContract.MovieDataContract.MOVIE_TITLE, title);
            cv.put(MovieContract.MovieDataContract.MOVIE_RATING,rating);

            cvArray[i] = cv;
        }

        return cvArray;
    }

}
