package com.markfeldman.popularmovies.utilities;

import android.content.ContentValues;

import com.markfeldman.popularmovies.database.MovieContract;

public class FakeData {

    public ContentValues[] getFakeData(){
        ContentValues[] cv = new ContentValues[2];

        cv[0].put(MovieContract.MovieDataContract.MOVIE_PREFERENCE, "Top Rated");
        cv[0].put(MovieContract.MovieDataContract.MOVIE_PLOT,"Guy gets candy");
        cv[0].put(MovieContract.MovieDataContract.MOVIE_RATING,"5.5");
        cv[0].put(MovieContract.MovieDataContract.MOVIE_POSTER_TAG,"/xq1Ugd62d23K2knRUx6xxuALTZB.jpg");
        cv[0].put(MovieContract.MovieDataContract.MOVIE_RELEASE,"May");
        cv[0].put(MovieContract.MovieDataContract.MOVIE_TITLE,"Candy Land");
        cv[0].put(MovieContract.MovieDataContract.MOVIE_ID, "297761");

        cv[1].put(MovieContract.MovieDataContract.MOVIE_PREFERENCE, "Most Popular");
        cv[1].put(MovieContract.MovieDataContract.MOVIE_PLOT,"Lady buys books");
        cv[1].put(MovieContract.MovieDataContract.MOVIE_RATING,"8.5");
        cv[1].put(MovieContract.MovieDataContract.MOVIE_POSTER_TAG,"/z09QAf8WbZncbitewNk6lKYMZsh.jpg");
        cv[1].put(MovieContract.MovieDataContract.MOVIE_RELEASE,"June");
        cv[1].put(MovieContract.MovieDataContract.MOVIE_TITLE,"Book Buyers");
        cv[1].put(MovieContract.MovieDataContract.MOVIE_ID, "14564");

        return cv;
    }
}
