package com.avans.avanstv.Data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.avans.avanstv.Domain.MovieList;

import java.util.List;

@Dao
public interface MovieListDao {
    @Insert
    void insert(MovieList movieList);

    @Query("DELETE FROM MovieList")
    void deleteAll();

    @Query("SELECT * FROM MovieList;")
    List<MovieList> getAllMovieLists();
}
