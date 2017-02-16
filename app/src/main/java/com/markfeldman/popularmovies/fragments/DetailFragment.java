package com.markfeldman.popularmovies.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.markfeldman.popularmovies.R;
import com.markfeldman.popularmovies.database.MovieContract;
import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {
    private TextView title;
    private ImageView moviePoster;
    private final String INTENT_EXTRA = "Intent Extra";
    private final String MOVIE_DB_URL_START = "http://image.tmdb.org/t/p/w185/";
    private String imageUrl;
    private String[] projection = {MovieContract.MovieDataContract._ID,MovieContract.MovieDataContract.MOVIE_TITLE,
            MovieContract.MovieDataContract.MOVIE_RELEASE,MovieContract.MovieDataContract.MOVIE_RATING,
            MovieContract.MovieDataContract.MOVIE_POSTER_TAG,MovieContract.MovieDataContract.MOVIE_PLOT};


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        //Get ID from Intent and add it to Content Provider URI
        //String stringId = Integer.toString(id);
        //Uri uri = TaskContract.TaskEntry.CONTENT_URI;
        //uri = uri.buildUpon().appendPath(stringId).build();
        //Return a Cursor pointing to a specific row from Content Provider using the id passed Intent
        //populate all Widgets with Data From this Row

        //Here you will use a Loader to get the link to the youtubeTrailer using your movie_id and place that
        //within a widget or launch youtube app with an implicit Intent
        //Like this maybe: http://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent

        title = (TextView)view.findViewById(R.id.movie_title);
        moviePoster = (ImageView)view.findViewById(R.id.movie_poster);

        Intent i = getActivity().getIntent();
        if (i!=null && i.hasExtra(INTENT_EXTRA)){
            //movieObj = i.getParcelableExtra(INTENT_EXTRA);
            Uri authority = MovieContract.MovieDataContract.CONTENT_URI;
            int id = i.getIntExtra(INTENT_EXTRA,0);
            String idConvert = Integer.toString(id);
            authority = authority.buildUpon().appendPath(idConvert).build();

            Cursor cursor = getActivity().getContentResolver().query(authority,projection,null,new String[]{idConvert},null);
            Picasso.with(getActivity()).load(MOVIE_DB_URL_START + cursor.getString(cursor.getColumnIndex(MovieContract.MovieDataContract.MOVIE_POSTER_TAG)))
                    .fit().into(moviePoster);
        }

        return view;
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }


    /* POSSIBLE WAY TO HANDLE THIS

    FIRST IN ONCREATE VIEW:
     LoaderManager.LoaderCallbacks<MovieObj[]> callbacks = this;

        Bundle bundleForLoader = null;
        getActivity().getSupportLoaderManager().initLoader(SEARCH_LOADER,null,callbacks);

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

     */
}
