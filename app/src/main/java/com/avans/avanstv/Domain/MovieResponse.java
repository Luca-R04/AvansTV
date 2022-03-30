package com.avans.avanstv.domain;

import java.util.List;

public class MovieResponse {
    private List<Movie> results;

    public MovieResponse(List<Movie> results) {
        this.results = results;
    }

    public List<Movie> getMovies() {
        return results;
    }
}
