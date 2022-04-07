package com.avans.avanstv.Data;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Domain.MovieList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieListRepository {
    private static volatile MovieListRepository INSTANCE;
    private static MutableLiveData<List<MovieList>> mMovieLists;
    private static MovieListDao mMovieListDao;

    private MovieListRepository(Application application) {
        MovieRoomDatabase db = MovieRoomDatabase.getDatabase(application);
        mMovieListDao = db.movieListDao();

        mMovieLists = new MutableLiveData<>();

        new GetMovieLists().execute();
    }

    public static MovieListRepository getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new MovieListRepository(application);
        }
        return INSTANCE;
    }

    public void insertMovieList(MovieList movieList) {
        new InsertMovieList().execute(movieList);
        new GetMovieLists().execute();
    }

    private static class InsertMovieList extends AsyncTask<MovieList, Void, Void> {

        @Override
        protected Void doInBackground(MovieList... movieLists) {
            mMovieListDao.insert(movieLists[0]);
            return null;
        }
    }

    public LiveData<List<MovieList>> getMovieLists() {
        return mMovieLists;
    }

    private static class GetMovieLists extends AsyncTask<Void, Void, List<MovieList>> {

        @Override
        protected List<MovieList> doInBackground(Void... voids) {
            return mMovieListDao.getAllMovieLists();
        }

        @Override
        protected void onPostExecute(List<MovieList> movieLists) {
            mMovieLists.setValue(movieLists);
        }
    }

    public void addMovieToList(String listName, Movie movie) {
        Log.i("MovieListRepository", "Called addMovieToList");
        Map<String, Movie> map = new HashMap<>();
        map.put(listName, movie);
        new AddMovieToList().execute(map);
    }

    private static class AddMovieToList extends AsyncTask<Map<String, Movie>, Void, MovieList> {

        @SafeVarargs
        @Override
        protected final MovieList doInBackground(Map<String, Movie>... maps) {
            String listName = maps[0].keySet().toString().replace("[", "").replace("]", "");
            for (MovieList movieList : mMovieLists.getValue()) {
                if (movieList != null) {
                    if (movieList.getName().equals(listName)) {
                        if (movieList.getMovies() == null) {
                            movieList.setMovies(new ArrayList<>());
                        }
                        movieList.addMovie(maps[0].get(listName));
                        mMovieListDao.setMovies(movieList.getMovies(), movieList.getId());
                        Log.i("AddMovieToList", "Added movie to list");
                        return movieList;
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieList movieList) {
            super.onPostExecute(movieList);
            if (movieList != null) {
                List<MovieList> movieLists = mMovieLists.getValue();
                assert movieLists != null;
                if (movieLists.contains(movieList)) {
                    movieLists.remove(movieList);
                    movieLists.add(movieList);
                }
                mMovieLists.setValue(movieLists);
            }
        }
    }
}