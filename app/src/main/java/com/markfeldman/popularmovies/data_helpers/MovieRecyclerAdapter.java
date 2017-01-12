package com.markfeldman.popularmovies.data_helpers;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.markfeldman.popularmovies.objects.MovieObj;
import com.squareup.picasso.Picasso;

import com.markfeldman.popularmovies.R;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieAdapterViewHolder> {
    private MovieObj[] movies;
    private final String MOVIE_DB_URL_START = "http://image.tmdb.org/t/p/w185/";
    private MovieClickedListener movieClickedListener;


    public interface MovieClickedListener{
        void onCLicked(MovieObj movieChosen);
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
        Context context = holder.movPoster.getContext();
        Picasso.with(context).load(MOVIE_DB_URL_START + movies[position].getMoviePosterTag()).fit().into(holder.movPoster);
    }

    @Override
    public int getItemCount() {
        if (null == movies) return 0;
        return movies.length;
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
            movieClickedListener.onCLicked(movies[adapterPosition]);
        }


    }

    public void setMovieData(MovieObj[] movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

}
