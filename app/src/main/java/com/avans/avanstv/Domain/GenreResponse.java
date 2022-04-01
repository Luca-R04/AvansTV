package com.avans.avanstv.Domain;

public class GenreResponse {
    private final String[] results;

    public GenreResponse(String[] results) {
        this.results = results;
    }

    public String[] getGenres() {
        return results;
    }
}
