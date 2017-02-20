package com.markfeldman.popularmovies.fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.markfeldman.popularmovies.R;
import com.markfeldman.popularmovies.database.MovieContract;
import com.markfeldman.popularmovies.utilities.JSONParser;
import com.markfeldman.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {
    private String returnedYouTubeKey;
    private String movieIdForYoutube;
    private final String INTENT_EXTRA = "Intent Extra";
    private final String MOVIE_DB_URL_START = "http://image.tmdb.org/t/p/w185/";
    private String[] projection = {MovieContract.MovieDataContract._ID,MovieContract.MovieDataContract.MOVIE_TITLE,
            MovieContract.MovieDataContract.MOVIE_RELEASE,MovieContract.MovieDataContract.MOVIE_RATING,
            MovieContract.MovieDataContract.MOVIE_POSTER_TAG,MovieContract.MovieDataContract.MOVIE_PLOT,
            MovieContract.MovieDataContract.MOVIE_ID};
    public final int ASYNC_LOADER_ID = 22;


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView title = (TextView)view.findViewById(R.id.movie_title);
        ImageView moviePoster = (ImageView)view.findViewById(R.id.movie_poster);
        TextView movieRelease = (TextView) view.findViewById(R.id.movie_release);
        TextView movieRating = (TextView)view.findViewById(R.id.movie_rating);
        TextView moviePlot = (TextView) view.findViewById(R.id.movie_synopsis);
        ImageView playBtn = (ImageView) view.findViewById(R.id.playButton);

        Intent i = getActivity().getIntent();
        if (i!=null && i.hasExtra(INTENT_EXTRA)){
            Uri authority = MovieContract.MovieDataContract.CONTENT_URI;
            int id = i.getIntExtra(INTENT_EXTRA,0);
            String idConvert = Integer.toString(id);
            authority = authority.buildUpon().appendPath(idConvert).build();

            Cursor cursor = getActivity().getContentResolver().query(authority,projection,null,new String[]{idConvert},null);

            Picasso.with(getActivity()).load(MOVIE_DB_URL_START + cursor.getString(cursor.getColumnIndex(MovieContract.MovieDataContract.MOVIE_POSTER_TAG)))
                    .fit().into(moviePoster);

            title.setText(cursor.getString(cursor.getColumnIndex(MovieContract.MovieDataContract.MOVIE_TITLE)));
            movieRelease.setText(cursor.getString(cursor.getColumnIndex(MovieContract.MovieDataContract.MOVIE_RELEASE)));
            movieRating.setText(cursor.getString(cursor.getColumnIndex(MovieContract.MovieDataContract.MOVIE_RATING)));
            moviePlot.setText(cursor.getString(cursor.getColumnIndex(MovieContract.MovieDataContract.MOVIE_PLOT)));

            movieIdForYoutube = cursor.getString(cursor.getColumnIndex(MovieContract.MovieDataContract.MOVIE_ID));
        }

        LoaderManager.LoaderCallbacks<String> callbacks = this;
        getActivity().getSupportLoaderManager().initLoader(ASYNC_LOADER_ID,null,callbacks);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchYoutubeVideo(returnedYouTubeKey);
            }
        });

        return view;
    }

    public void watchYoutubeVideo(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<String>(getActivity()) {
            String youTubeKey = null;
            @Override
            protected void onStartLoading() {
                if(youTubeKey!=null){
                    deliverResult(youTubeKey);
                }else {
                    forceLoad();
                }
                super.onStartLoading();
            }

            @Override
            public String loadInBackground() {
                try {
                    URL url = NetworkUtils.getURLForTrailer(movieIdForYoutube);
                    String httpResponse = NetworkUtils.getResponseFromHttpUrl(url);
                    youTubeKey= JSONParser.getYouTubeKeys(httpResponse);
                    return youTubeKey;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void deliverResult(String data) {
                youTubeKey = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        returnedYouTubeKey = data;
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
