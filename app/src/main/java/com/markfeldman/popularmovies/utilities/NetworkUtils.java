package com.markfeldman.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public final class NetworkUtils {
    private Context context;

    public NetworkUtils(){

    }

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private final static String BASE_URL = "http://api.themoviedb.org/";
    private final static String SEARCH_BY = "3/movie/";
    private final static String API_KEY_SEARCH = "?api_key=";
    private final static String API_KEY ="657b6c53f883538fe1f57b0e84031c09";
    private static String LANGUAGE_PARAM = "&language=";
    private static String LANGUAGE = "en-US";
    private static String PAGE_PARAM = "&page=";
    private static String PAGE_NUM = "1";

    //Have 2 mwthods that reurn URLs for both top rated and pop movies. Have the Json Parser parse
    //each individal and place in same ContentValues Object for provider

    public static URL getUrl(Context context) throws MalformedURLException {
        String prefCategory = MovSharedPreferences.getPreferredMovieCategory(context);
        Uri builtUri = buildURI(prefCategory);

        try{
            return new URL(builtUri.toString());
        }catch (MalformedURLException e){
            return null;
        }
    }

    private static Uri buildURI(String pref){
        return Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(SEARCH_BY + pref+ API_KEY_SEARCH + API_KEY + LANGUAGE_PARAM + LANGUAGE + PAGE_PARAM + PAGE_NUM)
                .build();
    }


    public static URL getURLForTrailer(String movieId) throws MalformedURLException {
        String movieSearch = movieId+"/videos";
        Uri uri = (buildURI(movieId));

        try {
            return new URL(buildUriForTrailer(movieId).toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Uri buildUriForTrailer(String movieId){
        return Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(SEARCH_BY + movieId + "/videos" + API_KEY_SEARCH + API_KEY + LANGUAGE_PARAM + LANGUAGE)
                .build();
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String httpResponseString ="";
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                httpResponseString = scanner.next();
            } else {
                return null;
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
        return httpResponseString;
    }
}
