package com.avans.avanstv.Data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.avans.avanstv.Domain.Genre;

@Dao
public interface GenreDao {
    @Insert
    void insert(Genre genre);

    @Query("DELETE FROM Genre")
    void deleteAll();

    @Query("SELECT * FROM Genre;")
    Genre[] getAllGenres();
}