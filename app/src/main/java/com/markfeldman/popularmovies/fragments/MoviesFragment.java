package com.markfeldman.popularmovies.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.markfeldman.popularmovies.R;
import com.markfeldman.popularmovies.activities.DetailActivity;
import com.markfeldman.popularmovies.utilities.JSONParser;
import com.markfeldman.popularmovies.utilities.MovieRecyclerAdapter;
import com.markfeldman.popularmovies.utilities.NetworkUtils;
import com.markfeldman.popularmovies.objects.MovieObj;
import org.json.JSONException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class MoviesFragment extends Fragment implements MovieRecyclerAdapter.MovieClickedListener, LoaderManager.LoaderCallbacks<String> {
    private RecyclerView recyclerView;
    private MovieRecyclerAdapter movieRecyclerAdapter;
    private final String INTENT_EXTRA = "Intent Extra";
    private TextView errorMessage;
    private final static int SEARCH_LOADER = 22;
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    private ProgressBar progressBar;

    public MoviesFragment() {
    }

    @Override
    public void onStart() {
        try {
            loadMovies();
        } catch (MalformedURLException e) {
            e.printStackTrace();
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
        return view;
    }

    public void loadMovies() throws MalformedURLException {
        URL urlResponse = NetworkUtils.buildUrlPopular();
        if (sortBy().equals("Top Rated")){
            urlResponse = NetworkUtils.buildUrlTopRated();
        }

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA,urlResponse.toString());

        //CHECK IF LOADER EXISTS
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();

        Loader<String> githubSearchLoader = loaderManager.getLoader(SEARCH_LOADER);
        if (githubSearchLoader == null) {
            loaderManager.initLoader(SEARCH_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(SEARCH_LOADER, queryBundle, this);
        }
    }

    @Override
    public void onCLicked(MovieObj movieChosen) {
        Intent i = new Intent(getActivity(), DetailActivity.class);
        i.putExtra(INTENT_EXTRA, movieChosen);
        startActivity(i);
    }

    private void showErrorMessage(){
        recyclerView.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    private void showDataView() {
        errorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return  new AsyncTaskLoader<String>(getActivity()) {
            String mJsonResult;
            @Override
            protected void onStartLoading() {
                progressBar.setVisibility(View.VISIBLE);
                super.onStartLoading();
                if (args == null){
                    return;
                }
                if (mJsonResult != null){
                    deliverResult(mJsonResult);
                }else{
                    forceLoad();
                }

            }

            @Override
            public String loadInBackground() {
                String searchQueryURLString = args.getString(SEARCH_QUERY_URL_EXTRA);
                if (searchQueryURLString==null){
                    return null;
                }
                try {
                    URL responseUrl = new URL(searchQueryURLString);
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(responseUrl);
                    return jsonResponse;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String data) {
                mJsonResult = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        MovieObj movies[] = null;
        progressBar.setVisibility(View.INVISIBLE);
        if (data == null){
            showErrorMessage();
        }else{
            JSONParser jsonParser = new JSONParser();
            try {
                movies = jsonParser.getMovieObjectsL(data);
                showDataView();
                movieRecyclerAdapter.setMovieData(movies);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    public String sortBy(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = prefs.getString(getString(R.string.movie_pref_title),getString(R.string.pref_default_unit));

        return sort;
    }
}
