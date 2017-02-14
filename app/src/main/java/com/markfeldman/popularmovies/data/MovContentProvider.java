package com.markfeldman.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.markfeldman.popularmovies.database.MovieContract;
import com.markfeldman.popularmovies.database.MovieDatabase;

public class MovContentProvider extends ContentProvider {
    public static final int CODE_DB = 100;
    public static final int CODE_DB_WITH_ID = 101;
    private MovieDatabase movieDatabase;
    private UriMatcher sUriMatcher = buildUriMatcher();

    public UriMatcher buildUriMatcher(){
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH,CODE_DB);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH + "/#",CODE_DB);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        movieDatabase = new MovieDatabase(getContext());
        return true;

    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.v("TAG", "IN QUERY!!!!!!!!");
        movieDatabase.openReadable();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match){
            case CODE_DB:{
                cursor = movieDatabase.getAllRows();
                break;
            }
            default:
                throw new UnsupportedOperationException("UNKOWN URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    //makes it clear that the method accepts null values,
    // and that if you override the method, you should also accept null values.
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        movieDatabase.openWritable();
        int match = sUriMatcher.match(uri);
        int rowsInserted=0;
        switch (match){
            case CODE_DB:{
                movieDatabase.beginTransaction();
                try{
                    for (ContentValues value : values){
                        long id = movieDatabase.insertRow(MovieContract.MovieDataContract.TABLE_NAME,value);
                        Log.v("TAG",value.getAsString(MovieContract.MovieDataContract.MOVIE_TITLE) + " ===== "+
                        value.getAsString(MovieContract.MovieDataContract.MOVIE_PLOT));
                        if (id!=-1){
                            rowsInserted++;
                        }
                    }

                    movieDatabase.transactionSuccesful();
                }finally {
                    movieDatabase.endTransaction();
                }
                if (rowsInserted>0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowsInserted;

            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        movieDatabase.openWritable();
        movieDatabase.deleteTable();
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
