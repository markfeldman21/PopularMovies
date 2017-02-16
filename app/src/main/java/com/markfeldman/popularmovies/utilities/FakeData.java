package com.markfeldman.popularmovies.utilities;

import android.content.ContentValues;

import com.markfeldman.popularmovies.database.MovieContract;

public class FakeData {
    ContentValues[] cv = new ContentValues[2];

    public ContentValues[] getFakeData(){


        ContentValues movieValues1 = new ContentValues();

        movieValues1.put(MovieContract.MovieDataContract.MOVIE_PLOT,"Guy gets candy");
        movieValues1.put(MovieContract.MovieDataContract.MOVIE_RATING,"5.5");
        movieValues1.put(MovieContract.MovieDataContract.MOVIE_POSTER_TAG,"/xq1Ugd62d23K2knRUx6xxuALTZB.jpg");
        movieValues1.put(MovieContract.MovieDataContract.MOVIE_RELEASE,"May");
        movieValues1.put(MovieContract.MovieDataContract.MOVIE_TITLE,"Candy Land");
        movieValues1.put(MovieContract.MovieDataContract.MOVIE_ID, "297761");

        cv[0] = movieValues1;

        ContentValues movieValues2 = new ContentValues();


        movieValues2.put(MovieContract.MovieDataContract.MOVIE_PLOT,"Lady buys books");
        movieValues2.put(MovieContract.MovieDataContract.MOVIE_RATING,"8.5");
        movieValues2.put(MovieContract.MovieDataContract.MOVIE_POSTER_TAG,"/z09QAf8WbZncbitewNk6lKYMZsh.jpg");
        movieValues2.put(MovieContract.MovieDataContract.MOVIE_RELEASE,"June");
        movieValues2.put(MovieContract.MovieDataContract.MOVIE_TITLE,"Book Buyers");
        movieValues2.put(MovieContract.MovieDataContract.MOVIE_ID, "14564");

        cv[1] = movieValues2;

        return cv;
    }
}
