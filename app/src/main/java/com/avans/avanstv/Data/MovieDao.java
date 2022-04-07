package com.avans.avanstv.Data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.avans.avanstv.Domain.Cast;
import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Domain.Video;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert
    void insert(Movie movie);

    @Query("DELETE FROM Movie")
    void deleteAll();

    @Query("UPDATE Movie SET favorite = :isFavorite WHERE movieId = :movieId")
    void setFavorite(int movieId, boolean isFavorite);

    @Query("UPDATE Movie SET youtubeVideo = :video WHERE movieId = :movieId")
    void setVideo(int movieId, Video video);

    @Query("UPDATE Movie SET `cast` = :cast WHERE movieId = :movieId")
    void setCast(int movieId, List<Cast> cast);

    @Query("SELECT * FROM Movie WHERE favorite = 1")
    List<Movie> getFavoriteMovies();

    @Query("SELECT * FROM Movie;")
    List<Movie> getAllMovies();

    @Query("SELECT * FROM Movie ORDER BY title ASC;")
    List<Movie> getMoviesASC();

    @Query("SELECT * FROM Movie ORDER BY title DESC;")
    List<Movie> getMoviesDESC();

    @Query("SELECT * FROM Movie ORDER BY release_date ASC;")
    List<Movie> getMoviesDateASC();

    @Query("SELECT * FROM Movie ORDER BY release_date DESC;")
    List<Movie> getMoviesDateDESC();

    @Query("SELECT * FROM Movie WHERE genre_ids = :genreId;")
    List<Movie> getMoviesByGenre(int genreId);

    @Query("UPDATE Movie SET `personalRating` = :personalRating WHERE movieId = :movieId")
    void setPersonalRating(int movieId, int personalRating);
}
