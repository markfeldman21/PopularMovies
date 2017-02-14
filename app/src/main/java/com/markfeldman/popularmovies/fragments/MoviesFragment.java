package com.markfeldman.popularmovies.fragments;


import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.markfeldman.popularmovies.R;
import com.markfeldman.popularmovies.activities.DetailActivity;
import com.markfeldman.popularmovies.database.MovieContract;
import com.markfeldman.popularmovies.database.MovieDatabase;
import com.markfeldman.popularmovies.utilities.FakeData;
import com.markfeldman.popularmovies.utilities.JSONParser;
import com.markfeldman.popularmovies.utilities.MovSharedPreferences;
import com.markfeldman.popularmovies.utilities.MovieRecyclerAdapter;
import com.markfeldman.popularmovies.utilities.NetworkUtils;
import com.markfeldman.popularmovies.objects.MovieObj;
import org.json.JSONException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class MoviesFragment extends Fragment implements MovieRecyclerAdapter.MovieClickedListener, LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener{
    private RecyclerView recyclerView;
    private MovieRecyclerAdapter movieRecyclerAdapter;
    private final String INTENT_EXTRA = "Intent Extra";
    private TextView errorMessage;
    private final static int SEARCH_LOADER_ID = 1;
    private ProgressBar progressBar;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
    private String[] projection = {MovieContract.MovieDataContract._ID,MovieContract.MovieDataContract.MOVIE_TITLE,
            MovieContract.MovieDataContract.MOVIE_RELEASE,MovieContract.MovieDataContract.MOVIE_RATING,
            MovieContract.MovieDataContract.MOVIE_POSTER_TAG,MovieContract.MovieDataContract.MOVIE_PLOT,
            MovieContract.MovieDataContract.MOVIE_PREFERENCE, MovieContract.MovieDataContract.MOVIE_ID};



    public MoviesFragment() {
    }

    @Override
    public void onStart() {
        if (PREFERENCES_HAVE_BEEN_UPDATED){

            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }

        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("MOVIEFRAG", "IN ON CREATE VIEW = ");
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        errorMessage = (TextView) view.findViewById(R.id.tv_error_message_display);
        progressBar = (ProgressBar)view.findViewById(R.id.pb_loading_indicator);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);


        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        movieRecyclerAdapter = new MovieRecyclerAdapter(this);
        recyclerView.setAdapter(movieRecyclerAdapter);

        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);

        //ADD FAKE DATA
        Cursor cursorTest = getActivity().getContentResolver().query(MovieContract.MovieDataContract.CONTENT_URI,
                projection,null,null,null);

        if (cursorTest == null){
            Log.d("MOVIEFRAG", "IN ON CREATE VIEW = CURSOR NULL!!!");
            ContentValues[] cv = new FakeData().getFakeData();
            getActivity().getContentResolver().bulkInsert(MovieContract.MovieDataContract.CONTENT_URI,cv);
        }else{
            Log.d("MOVIEFRAG", "IN ON CREATE VIEW = CURSOR NOT NULL!!!! ");
            getActivity().getContentResolver().delete(MovieContract.MovieDataContract.CONTENT_URI,null,null);
            ContentValues[] cv = new FakeData().getFakeData();
            getActivity().getContentResolver().bulkInsert(MovieContract.MovieDataContract.CONTENT_URI,cv);
        }


        getActivity().getSupportLoaderManager().initLoader(SEARCH_LOADER_ID, null, this);

        cursorTest.close();

        return view;
    }


    @Override
    public void onCLicked(MovieObj movieChosen) {
        Intent i = new Intent(getActivity(), DetailActivity.class);
        //Get ID of row and pass in Intent. Try using:
        // int id = (int) viewHolder.itemView.getTag();
        //Tag should be set in RecyclerViewAdapter
        i.putExtra(INTENT_EXTRA, movieChosen);
        startActivity(i);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
        Log.d("FORECASTFRAG", "PREF UPDATED!!!!!!!!");

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("LOADER", "IN CREATE LOADER!!!!!!!! ID ========" +id);
        switch (id){
            case SEARCH_LOADER_ID:{
                Log.d("LOADER", "IN CREATE LOADER CASE!!!!!!!!");
                Uri movieQueryUri = MovieContract.MovieDataContract.CONTENT_URI;

                return new CursorLoader(getActivity(),movieQueryUri,projection,null,null,null);
            }
            default:
                throw new RuntimeException("CURSOR LOADER NOT IMPLEMENTED " + id);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("LOADER", "IN FINISHED LOADER!!!!!!!!LENGTH ====" + data.getCount());
        movieRecyclerAdapter.swap(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieRecyclerAdapter.swap(null);
    }
}
