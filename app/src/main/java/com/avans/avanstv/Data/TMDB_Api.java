package com.avans.avanstv.Data;

import com.avans.avanstv.domain.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TMDB_Api {

    /** Authentication **/

    /** Movies **/

    //Get movie by id
    @GET("movie/{movie_id}?api_key={api_key}")
    Call<Movie> getMovieID(@Path("movie_id") int movie_id, @Path("api_key") String api_key);

    //Get movie by id + language
    @GET("movie/{movie_id}?api_key={api_key}&language={language}")
    Call<Movie> getMovieID(@Path("movie_id") int movie_id, @Path("api_key") String api_key, @Path("Language") String language);

}
