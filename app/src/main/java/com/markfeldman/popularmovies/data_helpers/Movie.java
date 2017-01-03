package com.markfeldman.popularmovies.data_helpers;

public class Movie {
    private String title;
    private String plot;
    private String rating;
    private String release;
    private String moviePosterTag;

    public Movie(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getMoviePosterTag() {
        return moviePosterTag;
    }

    public void setMoviePosterTag(String moviePosterTag) {
        this.moviePosterTag = moviePosterTag;
    }
}
