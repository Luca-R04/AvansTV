package com.avans.avanstv.Data;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avans.avanstv.Domain.Genre;
import com.avans.avanstv.Domain.GenreResponse;
import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Domain.MovieResponse;
import com.avans.avanstv.Domain.Video;
import com.avans.avanstv.Domain.VideoResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private static volatile MovieRepository INSTANCE;
    private static final String API_KEY = "f7c59563b1ecac0e4e6c1debf2a7485e";
    private static MutableLiveData<List<Movie>> mPopularMovies;
    private static MutableLiveData<List<Movie>> mTopRatedMovies;
    private static Genre[] mGenres;

    public MovieRepository() {
        new GetGenresFromAPI().execute();

        mPopularMovies = new MutableLiveData<>();
        new GetPopularMoviesFromAPI().execute();

        mTopRatedMovies = new MutableLiveData<>();
        new GetTopRatedMoviesFromAPI().execute();
    }

    public LiveData<List<Movie>> getLiveDataMovies() {
        return mPopularMovies;
    }

    public LiveData<List<Movie>> getTopRated() {
        return mTopRatedMovies;
    }

    public static void setVideosFromApi(int movieId) {
        new SetVideosFromAPI().execute(movieId);
    }

    public Genre[] getGenres() {
        return mGenres;
    }

    public static MovieRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MovieRepository();
        }
        return INSTANCE;
    }

    private static class GetPopularMoviesFromAPI extends AsyncTask<Void, Void, List<Movie>> {
        private final static String TAG = GetPopularMoviesFromAPI.class.getSimpleName();

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            try {
                Log.d(TAG, "doInBackground - retrieve all popular movies");

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org/3/")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                TMDB_Api service = retrofit.create(TMDB_Api.class);

                Log.d(TAG, "Calling getPopularMovies on service - attempt at retrieving the popular movies");
                Call<MovieResponse> call = service.getPopularMovies(API_KEY);
                Response<MovieResponse> response = call.execute();

                Log.d(TAG, "Executed call, response.code = " + response.code());

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d(TAG, "Good Response: " + response.body().getMovies());

                     for (Movie movie : response.body().getMovies()) {
                        MovieRepository.setVideosFromApi(movie.getId());
                     }

                    return response.body().getMovies();
                } else {
                    Log.d(TAG, "Bad Response: " + response.code());
                    return null;
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                mPopularMovies.setValue(movies);
            }
        }
    }

    private static class GetTopRatedMoviesFromAPI extends AsyncTask<Void, Void, List<Movie>> {
        private final static String TAG = GetTopRatedMoviesFromAPI.class.getSimpleName();

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            try {
                Log.d(TAG, "doInBackground - retrieve all popular movies");

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org/3/")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                TMDB_Api service = retrofit.create(TMDB_Api.class);

                Log.d(TAG, "Calling getPopularMovies on service - attempt at retrieving the popular movies");
                Call<MovieResponse> call = service.getTopRatedMovies(API_KEY);
                Response<MovieResponse> response = call.execute();

                Log.d(TAG, "Executed call, response.code = " + response.code());

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d(TAG, "Good Response: " + response.body().getMovies());

                    return response.body().getMovies();
                } else {
                    Log.d(TAG, "Bad Response: " + response.code());
                    return null;
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                mTopRatedMovies.setValue(movies);
            }
        }
    }

    private static class SetVideosFromAPI extends AsyncTask<Integer, Void, List<Video>> {
        private final static String TAG = SetVideosFromAPI.class.getSimpleName();

        @Override
        protected List<Video> doInBackground(Integer... integers) {
            Integer movieId = integers[0];

            try {
                Log.d(TAG, "doInBackground - retrieve all popular movies");

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org/3/")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                TMDB_Api service = retrofit.create(TMDB_Api.class);

                Log.d(TAG, "Calling getPopularMovies on service - attempt at retrieving the popular movies");
                Call<VideoResponse> call = service.getVideos(integers[0], API_KEY);
                Response<VideoResponse> response = call.execute();

                Log.d(TAG, "Executed call, response.code = " + response.code());

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<Video> videos = response.body().getVideos();
                    Log.d(TAG, "Good Response: " + videos);

                    for (Movie movie : mPopularMovies.getValue()) {
                        if (movie.getId() == movieId) {
                            movie.setYoutubeVideo(videos.get(0));
                        }
                    }

                    for (Movie movie : mTopRatedMovies.getValue()) {
                        if (movie.getId() == movieId) {
                            movie.setYoutubeVideo(videos.get(0));
                        }
                    }

                    return videos;
                } else {
                    Log.d(TAG, "Bad Response: " + response.code());
                    return null;
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e);
                return null;
            }
        }
    }

    private static class GetGenresFromAPI extends AsyncTask<Void, Void, Genre[]> {
        private final static String TAG = GetGenresFromAPI.class.getSimpleName();

        @Override
        protected Genre[] doInBackground(Void... voids) {
            try {
                Log.d(TAG, "doInBackground - retrieve all genres");
                Log.d(TAG, "Calling getMovieGenres on service - attempt at retrieving the genres");
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org/3/")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                TMDB_Api service = retrofit.create(TMDB_Api.class);

                Call<GenreResponse> call = service.getMovieGenres(API_KEY);
                Response<GenreResponse> response = call.execute();

                Log.d(TAG, "Executed call, response.code = " + response.code());

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d(TAG, "Good Response: " + Arrays.toString(response.body().getGenres()));
                    mGenres = response.body().getGenres();
                    return response.body().getGenres();
                } else {
                    Log.d(TAG, "Bad Response: " + response.code());
                    return null;
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e);
                return null;
            }
        }
    }
}