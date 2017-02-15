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
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.markfeldman.popularmovies.R;
import com.markfeldman.popularmovies.Sync.MovieSyncUtils;
import com.markfeldman.popularmovies.activities.DetailActivity;
import com.markfeldman.popularmovies.database.MovieContract;
import com.markfeldman.popularmovies.utilities.FakeData;
import com.markfeldman.popularmovies.utilities.MovieRecyclerAdapter;

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

        MovieSyncUtils.initialize(getActivity());

        getActivity().getSupportLoaderManager().initLoader(SEARCH_LOADER_ID, null, this);
        cursorTest.close();
        showLoading();
        return view;
    }


    @Override
    public void onCLicked(int rowId) {
        Intent i = new Intent(getActivity(), DetailActivity.class);
        //Get ID of row and pass in Intent. Try using:
        // int id = (int) viewHolder.itemView.getTag();
        //Tag should be set in RecyclerViewAdapter
        Toast.makeText(getActivity(), "ID PASS ==== " + rowId, Toast.LENGTH_LONG).show();

        i.putExtra(INTENT_EXTRA,rowId);
        startActivity(i);
    }

    public void showLoading(){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    public void showWeatherDataView(){
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void showErrorText(){
        recyclerView.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
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
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case SEARCH_LOADER_ID:{
                Uri movieQueryUri = MovieContract.MovieDataContract.CONTENT_URI;

                return new CursorLoader(getActivity(),movieQueryUri,projection,null,null,null);
            }
            default:
                throw new RuntimeException("CURSOR LOADER NOT IMPLEMENTED " + id);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        movieRecyclerAdapter.swap(data);
        if (data.getCount() != 0){
            showWeatherDataView();
        }else {
            showErrorText();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        movieRecyclerAdapter.swap(null);
    }
}
