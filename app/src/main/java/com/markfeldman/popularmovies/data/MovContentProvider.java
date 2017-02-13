package com.markfeldman.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

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
        int match = sUriMatcher.match(uri);
        switch (match){
            case CODE_DB:{
                break;
            }
            case CODE_DB_WITH_ID:{
                //use this to get ID passed in with URI String id = uri.getPathSegments().get(1);
                //Get Specific Row with all Data For Detail Fragment
                break;
            }
            default:
                throw new UnsupportedOperationException("UNKOWN URI: " + uri);
        }

        return null;
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
        int match = sUriMatcher.match(uri);
        int rowsInserted=0;
        switch (match){
            case CODE_DB:{
                try{
                    for (ContentValues value : values){
                        long id = movieDatabase.insertRow(MovieContract.MovieDataContract.TABLE_NAME,value);
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
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
