package com.avans.avanstv.Data;

import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Domain.MovieResponse;

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
}