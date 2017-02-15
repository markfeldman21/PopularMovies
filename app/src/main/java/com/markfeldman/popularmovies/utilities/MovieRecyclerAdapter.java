package com.markfeldman.popularmovies.utilities;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.markfeldman.popularmovies.database.MovieContract;
import com.markfeldman.popularmovies.objects.MovieObj;
import com.squareup.picasso.Picasso;

import com.markfeldman.popularmovies.R;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieAdapterViewHolder> {
    private MovieClickedListener movieClickedListener;
    private Cursor cursor;

    public interface MovieClickedListener{
        void onCLicked(int Id);
    }

    public MovieRecyclerAdapter(MovieClickedListener listener){
        this.movieClickedListener = listener;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.mov_poster;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        final String MOVIE_DB_URL_START = "http://image.tmdb.org/t/p/w185/";
        Context context = holder.movPoster.getContext();
        cursor.moveToPosition(position);
        String moviePoster = cursor.getString(cursor.getColumnIndex(MovieContract.MovieDataContract.MOVIE_POSTER_TAG));
        Picasso.with(context).load(MOVIE_DB_URL_START + moviePoster).fit().into(holder.movPoster);
    }

    @Override
    public int getItemCount() {
        if (cursor==null){
            return 0;
        }
        return cursor.getCount();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView movPoster;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            movPoster = (ImageView) itemView.findViewById(R.id.individual_movie);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            cursor.moveToPosition(adapterPosition);
            movieClickedListener.onCLicked(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieDataContract._ID)));
        }
    }

    public void swap (Cursor c){
        this.cursor = c;
        notifyDataSetChanged();
    }
}
