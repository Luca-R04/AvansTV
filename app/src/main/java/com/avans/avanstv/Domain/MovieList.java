package com.avans.avanstv.Domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class MovieList {
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
