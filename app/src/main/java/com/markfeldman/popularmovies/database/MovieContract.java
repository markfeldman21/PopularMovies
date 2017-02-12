package com.markfeldman.popularmovies.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {
    //Name for the entire content provider
    public static final String CONTENT_AUTHORITY = "com.markfeldman.popularmovies";
    //Uri for othet apps to be able to access
    public final static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //Possible paths that can be appended to BASE_CONTENT_URI to form valid URI
    public final static String PATH = "mov_database";

    public static final class MovieDataContract implements BaseColumns{
        //Used to Qury DB from provoder
        public final static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();
        public static final String TABLE_NAME = "mov_database";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_PLOT = "plot";
        public static final String MOVIE_RELEASE = "release";
        public static final String MOVIE_RATING = "rating";
        public static final String MOVIE_POSTER_TAG = "poster";
        public static final String MOVIE_PREFERENCE = "pref";

    }
}
