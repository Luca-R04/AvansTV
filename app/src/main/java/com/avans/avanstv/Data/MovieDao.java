package com.avans.avanstv.Data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.avans.avanstv.Domain.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert
    void insert(Movie movie);

    @Query("DELETE FROM Movie")
    void deleteAll();

    @Query("UPDATE Movie SET favorite = :isFavorite WHERE movieId = :movieId")
    void setFavorite(int movieId, boolean isFavorite);

    @Query("SELECT * FROM Movie WHERE favorite = 1")
    List<Movie> getFavoriteMovies();

    @Query("SELECT * FROM Movie;")
    List<Movie> getAllMovies();
}
