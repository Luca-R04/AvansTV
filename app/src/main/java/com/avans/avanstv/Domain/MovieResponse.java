package com.avans.avanstv.Domain;

import com.avans.avanstv.Domain.Movie;

import java.util.List;

public class MovieResponse {
    private final List<Movie> results;

    public MovieResponse(List<Movie> results) {
        this.results = results;
    }

    public List<Movie> getMovies() {
        return results;
    }
}
