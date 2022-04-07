package com.avans.avanstv.Data;

import static com.avans.avanstv.Presentation.MainActivity.getLanguage;

import android.app.Application;
import android.content.Context;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private static volatile MovieRepository INSTANCE;
    private static final String API_KEY = "f7c59563b1ecac0e4e6c1debf2a7485e";
    private static final String TAG = "MovieRepository";
    private static MutableLiveData<List<Movie>> mPopularMovies;
    private static MutableLiveData<List<Movie>> mCategoryMovies;
    private static MutableLiveData<List<Movie>> mTopRatedMovies;
    private static MutableLiveData<List<Movie>> mFavoritesMovieList;
    private static List<Movie> mAllMovies;
    private static List<Movie> mSearchResults;
    private static Genre[] mGenresAPI;
    private static Genre[] mGenresDatabase;
    private static GenreDao mGenreDao;
    private static MovieDao mMovieDao;
    private static NetworkInfo mNetworkInfo;
    private static int pageNumber = 1;
    private static final Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    private static final TMDB_Api service = retrofit.create(TMDB_Api.class);

    public MovieRepository(Application application) {
        MovieRoomDatabase db = MovieRoomDatabase.getDatabase(application);
        mMovieDao = db.movieDao();
        mGenreDao = db.genreDao();

        mPopularMovies = new MutableLiveData<>();
        mTopRatedMovies = new MutableLiveData<>();
        mFavoritesMovieList = new MutableLiveData<>();
        mCategoryMovies = new MutableLiveData<>();
        mAllMovies = new ArrayList<>();

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
        new GetFavoriteMovies().execute();
    }

    public static MovieRepository getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new MovieRepository(application);
        }
        return INSTANCE;
    }

    public boolean hasInternet() {
        return mNetworkInfo != null && mNetworkInfo.isConnected();
    }

    public static LiveData<List<Movie>> getLiveDataMovies() {
        return mPopularMovies;
    }

    public static LiveData<List<Movie>> getLiveDataCategoryMovies() {
        return mCategoryMovies;
    }

    public LiveData<List<Movie>> getTopRated() {
        return mTopRatedMovies;
    }

    public static void getMoreMovies() {
        pageNumber++;
        try {
            new GetPopularMoviesFromAPI().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void getCast(int movieID) {
        new GetCastFromAPI().execute(movieID);
    }

    public Genre[] getGenres() {
        if (mNetworkInfo == null || !mNetworkInfo.isConnected()) {
            return mGenresDatabase;
        }
        return mGenresAPI;
    }

    public static void setVideosFromApi(int movieId) {
        new SetVideosFromAPI().execute(movieId);
    }

    public List<Movie> searchMovie(String searchTerm) {
        new SearchMovie().execute(searchTerm);
        return mSearchResults;
    }

    public void setFavoriteMovie(Movie movie) {
        new SetFavoriteMovie().execute(movie);
        new GetFavoriteMovies().execute();
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return mFavoritesMovieList;
    }

    public static void setVideosForMovie(Movie movie) {
        new SetVideosForMovie().execute(movie);
    }

    public void setPersonalRatingForMovie(Movie movie) {
        new SetPersonalRatingDB().execute(movie);
    }

    public static void setCastForMovie(Movie movie) {
        new SetCastForMovie().execute(movie);
    }

    private static class GetPopularMoviesFromAPI extends AsyncTask<Void, Void, List<Movie>> {
        private final static String TAG = GetPopularMoviesFromAPI.class.getSimpleName();

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            try {
                Log.d(TAG, "doInBackground - retrieve all popular movies from the API");

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
                        MovieRepository.getCast(movie.getMovieId());
                        movie.setType("Popular");

                        if (movie.getYoutubeVideo() == null) {
                            Video video = new Video("");
                            movie.setYoutubeVideo(video);
                        }
                        mMovieDao.insert(movie);
                    }

                    mAllMovies.addAll(movies);

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

                String language = getLanguage();
                Call<MovieResponse> call = service.getTopRatedMovies(API_KEY, language);
                Response<MovieResponse> response = call.execute();

                Log.d(TAG, "Executed call, response.code = " + response.code());

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d(TAG, "Good Response: " + response.body().getMovies());
                    List<Movie> movies = response.body().getMovies();

                    mMovieDao.deleteAll();

                    for (Movie movie : movies) {
                        MovieRepository.setVideosFromApi(movie.getMovieId());
                        MovieRepository.getCast(movie.getMovieId());
                        movie.setType("TopRated");

                        if (movie.getYoutubeVideo() == null) {
                            Video video = new Video("");
                            movie.setYoutubeVideo(video);
                        }
                        mMovieDao.insert(movie);
                    }

                    mAllMovies.addAll(movies);

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

    private static class SetVideosFromAPI extends AsyncTask<Integer, Void, Void> {
        private final static String TAG = SetVideosFromAPI.class.getSimpleName();

        @Override
        protected Void doInBackground(Integer... integers) {
            Integer movieId = integers[0];

            try {
                Call<VideoResponse> call = service.getVideos(integers[0], API_KEY);
                Response<VideoResponse> response = call.execute();

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<Video> videos = response.body().getVideos();

                    for (Movie movie : mAllMovies) {
                        if (movie.getMovieId() == movieId) {
                            for (Video video : videos) {
                                movie.setYoutubeVideo(video);
                                MovieRepository.setVideosForMovie(movie);
                            }
                        }
                    }

                    if (mSearchResults != null) {
                        for (Movie movie : mSearchResults) {
                            if (movie.getMovieId() == movieId) {
                                for (Video video : videos) {
                                    movie.setYoutubeVideo(video);
                                    MovieRepository.setVideosForMovie(movie);
                                }
                            }
                        }
                    }

                } else {
                    Log.e(TAG, "Bad Response: " + response.code());
                }
                return null;
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

    private static class GetCastFromAPI extends AsyncTask<Integer, Void, Void> {
        private final static String TAG = GetCastFromAPI.class.getSimpleName();

        @Override
        protected Void doInBackground(Integer... integers) {
            Integer movieId = integers[0];

            try {

                Call<CastResponse> call = service.getMovieCast(integers[0], API_KEY);
                Response<CastResponse> response = call.execute();

                if (response.isSuccessful()) {
                    assert response.body() != null;

                    List<Cast> cast = response.body().getCast();
                    for (Movie movie : mAllMovies) {
                        if (movie.getMovieId() == movieId) {
                            movie.setCast(cast);
                            MovieRepository.setCastForMovie(movie);
                        }
                    }

                } else {
                    Log.d(TAG, "Bad Response: " + response.code());
                }
                return null;
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e);
                return null;
            }
        }
    }

    private static class SearchMovie extends AsyncTask<String, Void, List<Movie>> {
        private final static String TAG = SearchMovie.class.getSimpleName();

        @Override
        protected List<Movie> doInBackground(String... strings) {
            //There is no internet connection, so search in the database
            if (mNetworkInfo == null || !mNetworkInfo.isConnected()) {
                Log.d(TAG, "There is no internet connection, so search in the database " + strings[0].length());
                List<Movie> movies = new ArrayList<>();
                for (Movie movie : mAllMovies) {
                    Log.d(TAG, "There is no internet connection, so search in the database " + strings[0].length());
                    if (movie.getTitle().contains(strings[0]) || movie.getOverview().contains(strings[0])) {
                        movies.add(movie);
                        Log.d(TAG, "added movie " + movie.getTitle());
                    }
                }
                return movies;
            } else {
                //There is a internet connection, so search in the API
                try {
                    Log.d(TAG, "doInBackground - search for movies");

                    Log.d(TAG, strings[0]);
                    Call<MovieResponse> call = service.searchMovie(strings[0], API_KEY);
                    Response<MovieResponse> response = call.execute();

                    Log.d(TAG, "Executed call, response.code = " + response.code());

                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        Log.d(TAG, "Good Response: " + response.body().getMovies());

                        for (Movie movie : response.body().getMovies()) {
                            setVideosFromApi(movie.getMovieId());
                            getCast(movie.getMovieId());
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
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                mSearchResults = movies;
            }
        }
    }

    private static class SetFavoriteMovie extends AsyncTask<Movie, Void, Void> {

        @Override
        protected Void doInBackground(Movie... movies) {
            mMovieDao.setFavorite(movies[0].getMovieId(), movies[0].isFavorite());
            return null;
        }
    }

    private static class SetVideosForMovie extends AsyncTask<Movie, Void, Void> {

        @Override
        protected Void doInBackground(Movie... movies) {
            mMovieDao.setVideo(movies[0].getMovieId(), movies[0].getYoutubeVideo());
            return null;
        }
    }

    private static class SetCastForMovie extends AsyncTask<Movie, Void, Void> {

        @Override
        protected Void doInBackground(Movie... movies) {
            mMovieDao.setCast(movies[0].getMovieId(), movies[0].getCast());
            return null;
        }
    }

    private static class SetPersonalRatingDB extends AsyncTask<Movie, Void, Void> {

        @Override
        protected Void doInBackground(Movie... movies) {
            mMovieDao.setPersonalRating(movies[0].getMovieId(), movies[0].getPersonalRating());
            return null;
        }
    }

    private static class GetFavoriteMovies extends AsyncTask<Void, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            return mMovieDao.getFavoriteMovies();
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            mFavoritesMovieList.setValue(movies);
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

    public static class getMoviesAsc extends AsyncTask<Void, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(Void... voids) {
            return mMovieDao.getMoviesASC();
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            List<Movie> popularMovies = new ArrayList<>();
            if (movies != null) {
                for (Movie movie : movies) {
                    if (movie.getType().equals("Popular")) {
                        popularMovies.add(movie);
                    }
                }
                mPopularMovies.setValue(popularMovies);
            }
        }
    }

    public static class getMoviesDesc extends AsyncTask<Void, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(Void... voids) {
            return mMovieDao.getMoviesDESC();
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            List<Movie> popularMovies = new ArrayList<>();
            if (movies != null) {
                for (Movie movie : movies) {
                    if (movie.getType().equals("Popular")) {
                        popularMovies.add(movie);
                    }
                }
                mPopularMovies.setValue(popularMovies);
            }
        }
    }

    public static class getMoviesDateAsc extends AsyncTask<Void, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(Void... voids) {
            return mMovieDao.getMoviesDateASC();
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            List<Movie> popularMovies = new ArrayList<>();
            if (movies != null) {
                for (Movie movie : movies) {
                    if (movie.getType().equals("Popular")) {
                        popularMovies.add(movie);
                    }
                }
                mCategoryMovies.setValue(popularMovies);
            }
        }
    }

    public static class getMoviesDateDesc extends AsyncTask<Void, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(Void... voids) {
            return mMovieDao.getMoviesDateDESC();
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            List<Movie> popularMovies = new ArrayList<>();
            if (movies != null) {
                for (Movie movie : movies) {
                    if (movie.getType().equals("Popular")) {
                        popularMovies.add(movie);
                    }
                }
                mCategoryMovies.setValue(popularMovies);
            }
        }
    }

    public static class getMoviesByGenre extends AsyncTask<Integer, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(Integer... ints) {
            return mMovieDao.getMoviesByGenre(ints[0]);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            List<Movie> popularMovies = new ArrayList<>();
            if (movies != null) {
                for (Movie movie : movies) {
                    if (movie.getType().equals("Popular")) {
                        popularMovies.add(movie);
                    }
                }
                mCategoryMovies.setValue(popularMovies);
            }
        }
    }
}