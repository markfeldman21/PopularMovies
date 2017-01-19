package com.markfeldman.popularmovies.utilities;



import com.markfeldman.popularmovies.objects.MovieObj;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONParser {
    public JSONParser(){

    }

    public MovieObj[] getMovieObjectsL(String moviesJsonString) throws JSONException {
        final String MOVIEDB_RESULTS = "results";
        final String POSTER_PATH = "poster_path";

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray results =  jsonObject.getJSONArray(MOVIEDB_RESULTS);

        MovieObj movieObjs[] = new MovieObj[results.length()];

        for (int i = 0; i<results.length();i++){
            movieObjs[i] = new MovieObj();
            JSONObject individualMovie = results.getJSONObject(i);
            String moviePosterTag = individualMovie.getString(POSTER_PATH);
            movieObjs[i].setMoviePosterTag(moviePosterTag);

        }

        return movieObjs;
    }

}
