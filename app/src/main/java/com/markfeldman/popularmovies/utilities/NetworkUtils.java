package com.markfeldman.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

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

    final static String BASE_URL = "http://api.themoviedb.org/";
    final static String SEARCH_BY = "3/movie/";
    final static String API_KEY_SEARCH = "?api_key=";
    final static String API_KEY ="657b6c53f883538fe1f57b0e84031c09";
    final static String LANGUAGE_PARAM = "&language=";
    final static String LANGUAGE = "en-US";
    final static String PAGE_PARAM = "&page=";
    final static String PAGE_NUM = "1";

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


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
