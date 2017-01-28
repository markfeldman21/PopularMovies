package com.markfeldman.popularmovies.database;

import android.provider.BaseColumns;

public class MovieContract {

    public static final class MovieDataContract implements BaseColumns{
        public static final String TABLE_NAME = "mov_database";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_PLOT = "plot";
        public static final String MOVIE_RELEASE = "release";
        public static final String MOVIE_RATING = "rating";
        public static final String MOVIE_POSTER_TAG = "poster";

    }
}
