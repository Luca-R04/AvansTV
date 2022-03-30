package com.avans.avanstv.domain;

import java.util.List;

public class MovieResponse {
    private final List<Movie> results;

    public MovieResponse(List<Movie> results) {
        this.results = results;
    }

    public List<com.avans.avanstv.domain.Movie> getMovies() {
        return results;
    }
}
