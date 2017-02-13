package com.markfeldman.popularmovies.utilities;

import android.content.ContentValues;

import com.markfeldman.popularmovies.database.MovieContract;

/**
 * Created by markfeldman on 2/13/17.
 */

public class FakeData {

    public ContentValues[] getFakeData(){
        ContentValues[] cv = new ContentValues[2];

        cv[0].put(MovieContract.MovieDataContract.MOVIE_PREFERENCE, "Top Rated");
        cv[0].put(MovieContract.MovieDataContract.MOVIE_PLOT,"Guy gets candy");
        cv[0].put(MovieContract.MovieDataContract.MOVIE_RATING,"5.5");
        cv[0].put(MovieContract.MovieDataContract.MOVIE_POSTER_TAG,"/xq1Ugd62d23K2knRUx6xxuALTZB.jpg");
        cv[0].put(MovieContract.MovieDataContract.MOVIE_RELEASE,"May");
        cv[0].put(MovieContract.MovieDataContract.MOVIE_TITLE,"Candy Land");





        return cv;
    }
}
