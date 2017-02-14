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

    public MovieDatabase openWritable(){
        mDb = movieDatabaseHelper.getWritableDatabase();
        return this;
    }

    public MovieDatabase openReadable(){
        mDb = movieDatabaseHelper.getReadableDatabase();
        return this;
    }

    public void close(){
        mDb.close();
    }

    public void beginTransaction(){
        mDb.beginTransaction();
    }

    public void transactionSuccesful(){
        mDb.setTransactionSuccessful();
    }

    public void endTransaction(){
        mDb.endTransaction();
    }


    public Cursor getAllRows(){
        return mDb.query(MovieContract.MovieDataContract.TABLE_NAME,null,null,null,null,null,null);
    }

    public Cursor getSpecificRow(String tableName,String[] projection,String[] rowID){
        String where = MovieContract.MovieDataContract._ID + " = ?" + rowID;
        Cursor c = mDb.query(tableName,projection,where,rowID,null,null,null);
        if (c!=null){
            c.moveToFirst();
        }
        return c;
    }


    public void deleteTable(){
        mDb.delete(MovieContract.MovieDataContract.TABLE_NAME,null,null);
    }

    public void deleteAllRows(){
        Cursor c = getAllRows();
        int rowID = c.getColumnIndexOrThrow(MovieContract.MovieDataContract._ID);
        String idToDelete = Integer.toString(rowID);
        String[]args = {idToDelete};
        if (c.moveToFirst()){
            do{
                mDb.delete(MovieContract.MovieDataContract.TABLE_NAME," _id=?",args);
            }while(c.moveToNext());
        }
    }


    //SQLite return statement returns long containing id of inserted Row
    public long insertRow(String table, ContentValues cv){
        return mDb.insert(table, null,cv);
    }



    public static class MovieDatabaseHelper extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "waitlist.db";
        private static final int DATABASE_VERSION = 1;
        private final static String CREATE_DATABASE = "CREATE TABLE " + MovieContract.MovieDataContract.TABLE_NAME +
                " ("+ MovieContract.MovieDataContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieDataContract.MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieDataContract.MOVIE_PLOT + " TEXT NOT NULL, " +
                MovieContract.MovieDataContract.MOVIE_RATING + " REAL, " +
                MovieContract.MovieDataContract.MOVIE_RELEASE + " TEXT NOT NULL, " +
                MovieContract.MovieDataContract.MOVIE_POSTER_TAG + " TEXT NOT NULL, " +
                MovieContract.MovieDataContract.MOVIE_ID + " TEXT NOT NULL, " +
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
