package com.avans.avanstv.Data;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avans.avanstv.Data.TMDB_Api;
import com.avans.avanstv.domain.Movie;
import com.avans.avanstv.domain.MovieResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private static MutableLiveData<List<Movie>> mMovies;

    public MovieRepository() {
        mMovies = new MutableLiveData<>();
        new GetMoviesFromAPI().execute();
    }

    public LiveData<List<Movie>> getMovies() {
        return mMovies;
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
                Call<MovieResponse> call = service.getPopularMovies("f7c59563b1ecac0e4e6c1debf2a7485e");
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
        protected void onPostExecute(List<Movie> Movies) {
            if (Movies != null) {
                mMovies.setValue(Movies);
            }
        }
    }
}
