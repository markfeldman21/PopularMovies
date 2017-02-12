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
    private MovieDatabase movieDatabase;
    private UriMatcher sUriMatcher = buildUriMatcher();

    public UriMatcher buildUriMatcher(){
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH,CODE_DB);
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
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
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
