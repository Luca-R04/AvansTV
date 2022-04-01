package com.avans.avanstv.Domain;

public class GenreResponse {
    private final Genre[] genres;

    public GenreResponse(Genre[] genres) {
        this.genres = genres;
    }

    public Genre[] getGenres() {
        return genres;
    }
}
