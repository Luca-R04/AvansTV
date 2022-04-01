package com.avans.avanstv.Data;

import com.avans.avanstv.Domain.GenreResponse;
import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Domain.MovieResponse;
import com.avans.avanstv.Domain.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDB_Api {

    /** Authentication **/

    /** Movies **/
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String api_key);

    //Get movie by id
    @GET("movie/{movie_id}")
    Call<Movie> getMovieID(@Path("movie_id") int movie_id, @Query("api_key") String api_key);

    //Get movie by id + language
    @GET("movie/{movie_id}?language={language}")
    Call<Movie> getMovieID(@Path("movie_id") int movie_id, @Query("api_key") String api_key, @Path("Language") String language);

    //Get get top rated movies
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String api_key);

    //Get latest movies
    @GET("genre/movie/list")
    Call<GenreResponse> getMovieGenres(@Query("api_key") String api_key);

    //Get videos related to specific movie
    @GET("movie/{movie_id}/videos")
    Call<VideoResponse> getVideos(@Path("movie_id") int movie_id, @Query("api_key") String api_key);
}