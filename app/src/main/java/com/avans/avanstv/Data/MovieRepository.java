package com.avans.avanstv.Data;

import static com.avans.avanstv.Presentation.MainActivity.getLanguage;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avans.avanstv.Domain.Cast;
import com.avans.avanstv.Domain.CastResponse;
import com.avans.avanstv.Domain.Genre;
import com.avans.avanstv.Domain.GenreResponse;
import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Domain.MovieResponse;
import com.avans.avanstv.Domain.Video;
import com.avans.avanstv.Domain.VideoResponse;
import com.avans.avanstv.Presentation.MainActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private static volatile MovieRepository INSTANCE;
    private static final String API_KEY = "f7c59563b1ecac0e4e6c1debf2a7485e";
    private static final String TAG = "MovieRepository";
    private static MutableLiveData<List<Movie>> mPopularMovies;
    private static MutableLiveData<List<Movie>> mTopRatedMovies;
    private static List<Movie> mSearchResults;
    private static Genre[] mGenresAPI;
    private static Genre[] mGenresDatabase;
    private static List<Cast> mCastAPI;
    private static List<Cast> mCastDatabase;
    private static GenreDao mGenreDao;
    private static MovieDao mMovieDao;
    private static NetworkInfo mNetworkInfo;
    private static int pageNumber = 1;

    private MovieRepository(Application application) {
        MovieRoomDatabase db = MovieRoomDatabase.getDatabase(application);
        mMovieDao = db.movieDao();
        mGenreDao = db.genreDao();

        mPopularMovies = new MutableLiveData<>();
        mTopRatedMovies = new MutableLiveData<>();

        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            mNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }

        if (mNetworkInfo == null || !mNetworkInfo.isConnected()) {
            Log.i(TAG, "There is no internet connection, retrieve data from the local database.");
            new GetGenresFromDatabase().execute();
            new GetMoviesFromDatabase().execute();
        } else {
            Log.i(TAG, "There is a internet connection, retrieve data from API.");
            new GetGenresFromAPI().execute();
            new GetTopRatedMoviesFromAPI().execute();
            new GetPopularMoviesFromAPI().execute();
        }
    }

    public static LiveData<List<Movie>> getLiveDataMovies() {
        return mPopularMovies;
    }

    public LiveData<List<Movie>> getTopRated() {
        return mTopRatedMovies;
    }

    public static void setVideosFromApi(int movieId) {
        new SetVideosFromAPI().execute(movieId);
    }

    public Genre[] getGenres() {
        if (mNetworkInfo == null || !mNetworkInfo.isConnected()) {
            return mGenresDatabase;
        }
        return mGenresAPI;
    }

    public List<Movie> searchMovie(String searchTerm) {
        new SearchMovie().execute(searchTerm);
        return mSearchResults;
    }

    public static void getMoreMovies() {
        pageNumber++;
        new GetPopularMoviesFromAPI().execute();
    }

    public static MovieRepository getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new MovieRepository(application);
        }
        return INSTANCE;
    }

    private static class GetPopularMoviesFromAPI extends AsyncTask<Void, Void, List<Movie>> {
        private final static String TAG = GetPopularMoviesFromAPI.class.getSimpleName();

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            try {
                Log.d(TAG, "doInBackground - retrieve all popular movies from the API");

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org/3/")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                TMDB_Api service = retrofit.create(TMDB_Api.class);

                //Get language from SavedPreference
                String language = getLanguage();


                Call<MovieResponse> call = service.getPopularMovies(API_KEY, pageNumber, language);
                Response<MovieResponse> response = call.execute();

                Log.d(TAG, "Executed call, response.code = " + response.code());

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d(TAG, "Good Response: " + response.body().getMovies());
                    List<Movie> movies = response.body().getMovies();

                    for (Movie movie : movies) {
                        MovieRepository.setVideosFromApi(movie.getMovieId());
                        movie.setType("Popular");
                    }

                    return movies;
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
                Log.d(TAG, "doInBackground - retrieve all top rated movies from API");

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org/3/")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                TMDB_Api service = retrofit.create(TMDB_Api.class);

                String language = getLanguage();
                Call<MovieResponse> call = service.getTopRatedMovies(API_KEY, language);
                Response<MovieResponse> response = call.execute();

                Log.d(TAG, "Executed call, response.code = " + response.code());

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d(TAG, "Good Response: " + response.body().getMovies());
                    List<Movie> movies = response.body().getMovies();

                    for (Movie movie : movies) {
                        MovieRepository.setVideosFromApi(movie.getMovieId());
                        movie.setType("TopRated");
                    }

                    return movies;
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
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org/3/")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                TMDB_Api service = retrofit.create(TMDB_Api.class);

                Call<VideoResponse> call = service.getVideos(integers[0], API_KEY);
                Response<VideoResponse> response = call.execute();

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<Video> videos = response.body().getVideos();

                    mMovieDao.deleteAll();

                    for (Movie movie : mPopularMovies.getValue()) {
                        if (movie.getMovieId() == movieId) {
                            for (Video video : videos) {
                                movie.setYoutubeVideo(video);
                            }
                        }
                        if (movie.getYoutubeVideo() == null) {
                            Video video = new Video("");
                            movie.setYoutubeVideo(video);
                        }
                        mMovieDao.insert(movie);
                    }

                    for (Movie movie : mTopRatedMovies.getValue()) {
                        if (movie.getMovieId() == movieId) {
                            for (Video video : videos) {
                                movie.setYoutubeVideo(video);
                            }
                        }
                        //This needs to be done for the Room database, otherwise it won't save the movie
                        if (movie.getYoutubeVideo() == null) {
                            Video video = new Video("");
                            movie.setYoutubeVideo(video);
                        }
                        mMovieDao.insert(movie);
                    }

                    return videos;
                } else {
                    Log.e(TAG, "Bad Response: " + response.code());
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
                    mGenreDao.deleteAll();

                    mGenresAPI = response.body().getGenres();
                    for (Genre genre : mGenresAPI) {
                        mGenreDao.insert(genre);
                    }

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

//    private static class GetCastFromAPI extends AsyncTask<Integer, Void, List<Cast>> {
//        private final static String TAG = GetGenresFromAPI.class.getSimpleName();
//
//        @Override
//        protected List<Cast> doInBackground(Integer... integers) {
//            Integer movieId = integers[0];
//
//            try {
//                Log.d(TAG, "doInBackground - retrieve all genres");
//                Gson gson = new GsonBuilder()
//                        .setLenient()
//                        .create();
//
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("https://api.themoviedb.org/3/")
//                        .addConverterFactory(GsonConverterFactory.create(gson))
//                        .build();
//
//                TMDB_Api service = retrofit.create(TMDB_Api.class);
//
//                Call<CastResponse> call = service.getMovieCast(integers[0], API_KEY);
//                Response<CastResponse> response = call.execute();
//
//                Log.d(TAG, "Executed call, response.code = " + response.code());
//
//                if (response.isSuccessful()) {
//                    assert response.body() != null;
//                    Log.d(TAG, "Good Response: " + response.body().getCast());
//                    mCastDatabase.deleteAll();
//
//                    mCastAPI = response.body().getCast();
//                    for (Genre genre : mGenresAPI) {
//                        mGenreDao.insert(genre);
//                    }
//
//                    for (Movie movie : mPopularMovies.getValue()) {
//                        if (movie.getMovieId() == movieId) {
//                            movie.setCast(mCastAPI);
//                        }
//                    }
//
//                    return response.body().getCast();
//                } else {
//                    Log.d(TAG, "Bad Response: " + response.code());
//                    return null;
//                }
//            } catch (Exception e) {
//                Log.e(TAG, "Exception: " + e);
//                return null;
//            }
//        }
//    }

    private static class SearchMovie extends AsyncTask<String, Void, List<Movie>> {
        private final static String TAG = SearchMovie.class.getSimpleName();

        @Override
        protected List<Movie> doInBackground(String... strings) {
            try {
                Log.d(TAG, "doInBackground - retrieve all genres");
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org/3/")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                TMDB_Api service = retrofit.create(TMDB_Api.class);

                Log.d(TAG, strings[0]);
                Call<MovieResponse> call = service.searchMovie(strings[0], API_KEY);
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
                mSearchResults = movies;
            }
        }
    }


    private static class GetGenresFromDatabase extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            mGenresDatabase = mGenreDao.getAllGenres();
            return null;
        }
    }

    private static class GetMoviesFromDatabase extends AsyncTask<Void, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            return mMovieDao.getAllMovies();
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            List<Movie> popularMovies = new ArrayList<>();
            List<Movie> topRatedMovies = new ArrayList<>();
            if (movies != null) {
                for (Movie movie : movies) {
                    if (movie.getType().equals("Popular")) {
                        popularMovies.add(movie);
                    } else {
                        topRatedMovies.add(movie);
                    }
                }
                mPopularMovies.setValue(popularMovies);
                mTopRatedMovies.setValue(topRatedMovies);
            }
        }
    }
}