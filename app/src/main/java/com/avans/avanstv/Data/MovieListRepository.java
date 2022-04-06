package com.avans.avanstv.Data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avans.avanstv.Domain.MovieList;

import java.util.List;

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
}