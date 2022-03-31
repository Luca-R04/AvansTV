package com.avans.avanstv.Data;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Domain.MovieResponse;
import com.avans.avanstv.Presentation.HomeFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private static MutableLiveData<List<Movie>> mLiveDataMovies;
    private static List<Movie> mMovies;
    private static volatile MovieRepository INSTANCE;
    private static final String API_KEY = "f7c59563b1ecac0e4e6c1debf2a7485e";

    public MovieRepository() {
        mLiveDataMovies = new MutableLiveData<>();
//        new GetMoviesFromAPI().execute();
        getPopularMovies();
    }

    public LiveData<List<Movie>> getLiveDataMovies() {
        return mLiveDataMovies;
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    public static MovieRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MovieRepository();
        }
        return INSTANCE;
    }

    public void getPopularMovies() {
        Log.d("MovieRepository", "getPopularMovies is called");
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        TMDB_Api service = retrofit.create(TMDB_Api.class);

        Call<MovieResponse> call = service.getPopularMovies(API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                List<Movie> movies = response.body().getMovies();
                mLiveDataMovies.setValue(movies);

                mMovies = movies;
                Log.d("MovieRepository", "onPostExecute retrieved movies!");

                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setRandomMovie(mMovies);
                Log.d("MovieRepository", "Set random movie for HomeFragment!");
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Log.d("MovieRepository", "Error logging in: " + t.getMessage());
            }
        });
    }


    private static class GetMoviesFromAPI extends AsyncTask<Void, Void, List<Movie>> {
        private final static String TAG = GetMoviesFromAPI.class.getSimpleName();

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
                mLiveDataMovies.setValue(movies);
                mMovies = movies;
            }
        }
    }
}
