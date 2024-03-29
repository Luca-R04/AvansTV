package com.avans.avanstv.Data;

import com.avans.avanstv.Domain.CastResponse;
import com.avans.avanstv.Domain.GenreResponse;
import com.avans.avanstv.Domain.MovieResponse;
import com.avans.avanstv.Domain.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDB_Api {

    /** Movies **/
    //Get popular movies
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String api_key, @Query("page") int page, @Query("language") String language);

    //Get get top rated movies
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String api_key, @Query("language") String language);

    //Search for a movie
    @GET("search/movie")
    Call<MovieResponse> searchMovie(@Query("query") String searchTerm, @Query("api_key") String api_key);

    //Get movie genres
    @GET("genre/movie/list")
    Call<GenreResponse> getMovieGenres(@Query("api_key") String api_key);

    //Get movie cast
    @GET("movie/{movie_id}/credits")
    Call<CastResponse> getMovieCast(@Path("movie_id") int movie_id, @Query("api_key") String api_key);

    //Get videos related to specific movie
    @GET("movie/{movie_id}/videos")
    Call<VideoResponse> getVideos(@Path("movie_id") int movie_id, @Query("api_key") String api_key);
}