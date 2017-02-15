package com.markfeldman.popularmovies.Sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.markfeldman.popularmovies.database.MovieContract;

public class MovieSyncUtils {

    synchronized public static void initialize (final Context context){


        //What is the class of the object that we've passed to Thread() exactly?
        // It's not an instance of Runnable; Runnable is just an interface.
        // It's an instance of a class that has no name -- an anonymous class -- that implements Runnable.
        //https://www.caveofprogramming.com/javatutorial/java-tutorial-8-starting-threads-and-using-anonymous-classes.html
        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                Uri uri = MovieContract.MovieDataContract.CONTENT_URI;
                String[] projection = {MovieContract.MovieDataContract._ID,MovieContract.MovieDataContract.MOVIE_TITLE,
                        MovieContract.MovieDataContract.MOVIE_RELEASE,MovieContract.MovieDataContract.MOVIE_RATING,
                        MovieContract.MovieDataContract.MOVIE_POSTER_TAG,MovieContract.MovieDataContract.MOVIE_PLOT,
                        MovieContract.MovieDataContract.MOVIE_PREFERENCE, MovieContract.MovieDataContract.MOVIE_ID};

                Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);

                if (cursor == null|| cursor.getCount()==0){
                    Intent i = new Intent(context, MovieIntentService.class);
                    context.startService(i);
                }
                cursor.close();
            }
        });
        checkForEmpty.start();
    }

}
