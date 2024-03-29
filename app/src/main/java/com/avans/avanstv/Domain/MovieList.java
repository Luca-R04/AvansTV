package com.avans.avanstv.Domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity
public class MovieList implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String mName;
    private List<Movie> mMovies;

    public MovieList(String name) {
        this.mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    public void setMovies(List<Movie> mMovies) {
        this.mMovies = mMovies;
    }

    public void addMovie(Movie movie) {
        this.mMovies.add(movie);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
