package com.markfeldman.popularmovies.fragments;


import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.markfeldman.popularmovies.R;
import com.markfeldman.popularmovies.Sync.MovieSyncUtils;
import com.markfeldman.popularmovies.activities.DetailActivity;
import com.markfeldman.popularmovies.database.MovieContract;
import com.markfeldman.popularmovies.utilities.FakeData;
import com.markfeldman.popularmovies.utilities.MovieRecyclerAdapter;
import com.markfeldman.popularmovies.utilities.NotificationUtils;

public class MoviesFragment extends Fragment implements MovieRecyclerAdapter.MovieClickedListener, LoaderManager.LoaderCallbacks<Cursor>
        {
    private RecyclerView recyclerView;
    private MovieRecyclerAdapter movieRecyclerAdapter;
    private ProgressBar progressBar;
    private final String INTENT_EXTRA = "Intent Extra";
    private TextView errorMessage;
    private final static int SEARCH_LOADER_ID = 1;
    private String[] projection = {MovieContract.MovieDataContract._ID,MovieContract.MovieDataContract.MOVIE_TITLE,
            MovieContract.MovieDataContract.MOVIE_RELEASE,MovieContract.MovieDataContract.MOVIE_RATING,
            MovieContract.MovieDataContract.MOVIE_POSTER_TAG,MovieContract.MovieDataContract.MOVIE_PLOT,
            MovieContract.MovieDataContract.MOVIE_ID};

    public MoviesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        errorMessage = (TextView) view.findViewById(R.id.tv_error_message_display);
        progressBar = (ProgressBar)view.findViewById(R.id.pb_loading_indicator);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        movieRecyclerAdapter = new MovieRecyclerAdapter(this);
        recyclerView.setAdapter(movieRecyclerAdapter);
        Button button = (Button)view.findViewById(R.id.testbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationUtils.notifyUser(getActivity());
            }
        });


        MovieSyncUtils.initialize(getActivity());
        getActivity().getSupportLoaderManager().initLoader(SEARCH_LOADER_ID, null, this);
        return view;
    }


    @Override
    public void onCLicked(int rowId) {
        Intent i = new Intent(getActivity(), DetailActivity.class);
        i.putExtra(INTENT_EXTRA,rowId);
        startActivity(i);
    }

    public void loadSuccess(){
        errorMessage.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void loadFailed(){
        errorMessage.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
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
        if (data.getCount() != 0){
            movieRecyclerAdapter.swap(data);
            loadSuccess();
        }else if (data.getCount() == 0) {
            loadFailed();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        movieRecyclerAdapter.swap(null);
    }
}
