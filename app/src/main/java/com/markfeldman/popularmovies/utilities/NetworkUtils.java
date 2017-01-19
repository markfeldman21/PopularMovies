package com.markfeldman.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;

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
    final static String SEARCH_BY_POP = "3/movie/popular";
    final static String SEARCH_BY_TOP = "3/movie/top_rated";
    final static String API_KEY_SEARCH = "?api_key=";
    final static String API_KEY ="657b6c53f883538fe1f57b0e84031c09";
    final static String LANGUAGE_PARAM = "&language=";
    final static String LANGUAGE = "en-US";
    final static String PAGE_PARAM = "&page=";
    final static String PAGE_NUM = "1";


    public static URL buildUrlPopular() throws MalformedURLException {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(SEARCH_BY_POP + API_KEY_SEARCH + API_KEY + LANGUAGE_PARAM + LANGUAGE + PAGE_PARAM + PAGE_NUM)
                .build();
        URL url = new URL(builtUri.toString());

        return url;
    }

    public static URL buildUrlTopRated() throws MalformedURLException {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(SEARCH_BY_TOP + API_KEY_SEARCH + API_KEY + LANGUAGE_PARAM + LANGUAGE + PAGE_PARAM + PAGE_NUM)
                .build();
        URL url = new URL(builtUri.toString());

        return url;
    }


    public static URL buildUrl(Double lat, Double lon) {
        /** This will be implemented in a future lesson **/
        return null;
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
