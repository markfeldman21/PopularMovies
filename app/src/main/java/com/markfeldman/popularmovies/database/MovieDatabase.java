package com.markfeldman.popularmovies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDatabase {
    private SQLiteDatabase mDb;
    private MovieDatabaseHelper movieDatabaseHelper;
    private Context context;

    public MovieDatabase(Context context){
        this.context = context;
        movieDatabaseHelper = new MovieDatabaseHelper(context);
    }

    public MovieDatabase open(){
        mDb = movieDatabaseHelper.getWritableDatabase();
        return this;
    }

    public Cursor getAllRows(){
        return mDb.query(MovieContract.MovieDataContract.TABLE_NAME,null,null,null,null,null,null);
    }

    public void deleteRow(long aLong){

    }

    public void deleteTable(){
        mDb.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieDataContract.TABLE_NAME);
    }

    public void deleteAllRows(){
        Cursor c = getAllRows();
        long rowID = c.getColumnIndexOrThrow(MovieContract.MovieDataContract._ID);
        if (c.moveToFirst()){
            do{
                deleteRow(c.getLong((int) rowID));
            }while(c.moveToNext());
        }
    }

    //SQLite return statement returns long containing id of inserted Row
    public long insertRow(String title, String plot, Double rating, String release, String poster, String pref){
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieDataContract.MOVIE_TITLE, title);
        cv.put(MovieContract.MovieDataContract.MOVIE_PLOT, plot);
        cv.put(MovieContract.MovieDataContract.MOVIE_RATING,rating);
        cv.put(MovieContract.MovieDataContract.MOVIE_RELEASE,release);
        cv.put(MovieContract.MovieDataContract.MOVIE_POSTER_TAG,poster);
        cv.put(MovieContract.MovieDataContract.MOVIE_PREFERENCE,pref);

        return mDb.insert(MovieContract.MovieDataContract.TABLE_NAME, null,cv);
    }


    public static class MovieDatabaseHelper extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "waitlist.db";
        private static final int DATABASE_VERSION = 1;
        private final static String CREATE_DATABASE = "CREATE TABLE " + MovieContract.MovieDataContract.TABLE_NAME +
                " ("+ MovieContract.MovieDataContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieDataContract.MOVIE_TITLE + "TEXT NOT NULL, " +
                MovieContract.MovieDataContract.MOVIE_PLOT + " TEXT NOT NULL, " +
                MovieContract.MovieDataContract.MOVIE_RATING + " REAL, " +
                MovieContract.MovieDataContract.MOVIE_RELEASE + " TEXT NOT NULL, " +
                MovieContract.MovieDataContract.MOVIE_POSTER_TAG + " TEXT NOT NULL, " +
                MovieContract.MovieDataContract.MOVIE_PREFERENCE + " TEXT NOT NULL" +
                ");";


        public MovieDatabaseHelper(Context context){
            super (context, DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DATABASE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieDataContract.TABLE_NAME);
            onCreate(db);

        }
    }
}
