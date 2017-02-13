package com.markfeldman.popularmovies.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
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
import com.markfeldman.popularmovies.database.MovieDatabase;
import com.markfeldman.popularmovies.utilities.JSONParser;
import com.markfeldman.popularmovies.utilities.MovSharedPreferences;
import com.markfeldman.popularmovies.utilities.MovieRecyclerAdapter;
import com.markfeldman.popularmovies.utilities.NetworkUtils;
import com.markfeldman.popularmovies.objects.MovieObj;
import org.json.JSONException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class MoviesFragment extends Fragment implements MovieRecyclerAdapter.MovieClickedListener, LoaderManager.LoaderCallbacks<MovieObj[]>,
        SharedPreferences.OnSharedPreferenceChangeListener{
    private RecyclerView recyclerView;
    private MovieRecyclerAdapter movieRecyclerAdapter;
    private final String INTENT_EXTRA = "Intent Extra";
    private TextView errorMessage;
    private final static int SEARCH_LOADER = 1;
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    private ProgressBar progressBar;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;



    public MoviesFragment() {
    }

    @Override
    public void onStart() {
        if (PREFERENCES_HAVE_BEEN_UPDATED){
            getActivity().getSupportLoaderManager().restartLoader(SEARCH_LOADER,null,this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }

        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("FORCAST", "IN ON CREATE VIEW = ");
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        errorMessage = (TextView) view.findViewById(R.id.tv_error_message_display);
        progressBar = (ProgressBar)view.findViewById(R.id.pb_loading_indicator);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        movieRecyclerAdapter = new MovieRecyclerAdapter(this);
        recyclerView.setAdapter(movieRecyclerAdapter);

        LoaderManager.LoaderCallbacks<MovieObj[]> callbacks = this;

        Bundle bundleForLoader = null;
        getActivity().getSupportLoaderManager().initLoader(SEARCH_LOADER,null,callbacks);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);

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

    private void showErrorMessage(){
        recyclerView.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    private void showDataView() {
        progressBar.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public Loader<MovieObj[]> onCreateLoader(int id, final Bundle args) {
        return  new AsyncTaskLoader<MovieObj[]>(getActivity()) {
            MovieObj[] movies = null;
            @Override
            protected void onStartLoading() {
                progressBar.setVisibility(View.VISIBLE);
                if (movies!=null){
                    progressBar.setVisibility(View.INVISIBLE);
                    deliverResult(movies);
                } else{
                    forceLoad();
                }
                super.onStartLoading();

            }

            @Override
            public MovieObj[] loadInBackground() {
                String searchQueryURLString = MovSharedPreferences.getPreferredMovieCategory(getActivity());
                if (searchQueryURLString==null){
                    return null;
                }
                try {
                    URL movieRequest = NetworkUtils.buildUrl(searchQueryURLString);
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(movieRequest);
                    JSONParser jsonParser = new JSONParser();
                    movies = jsonParser.getMovieObjectsL(jsonResponse);
                    return movies;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(MovieObj[] data) {
                movies = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<MovieObj[]> loader, MovieObj[] data) {
        if (data == null){
            showErrorMessage();
        }else{
            showDataView();
            movieRecyclerAdapter.setMovieData(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<MovieObj[]> loader) {

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
        Log.d("FORECASTFRAG", "PREF UPDATED!!!!!!!!");

    }

    public void saveToDatabase(MovieObj[] movieObjs){
        MovieDatabase movieDatabase = new MovieDatabase(getActivity());
        movieDatabase.open();

    }
}
