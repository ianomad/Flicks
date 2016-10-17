/*
 * Copyright (c) 2016.
 * Iliiazbek Akhmedov
 *
 */

package codepath.flicks.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResponse {

    private int page;

    @SerializedName("results")
    private List<Movie> movies;

    public MoviesResponse() {

    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
