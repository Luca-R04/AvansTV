package com.avans.avanstv.Presentation.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.avans.avanstv.Data.MovieListRepository;
import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Domain.MovieList;

import java.util.List;

public class MovieListViewModel extends AndroidViewModel {
    private final LiveData<List<MovieList>> mMovieLists;
    private final LiveData<List<Movie>> mFavoriteMoviesList;
    private final MovieListRepository mMovieListRepository;

    public MovieListViewModel(@NonNull Application application) {
        super(application);
        mMovieListRepository = MovieListRepository.getInstance(application);
        MovieRepository mMovieRepository = MovieRepository.getInstance(application);

        mMovieLists = mMovieListRepository.getMovieLists();
        mFavoriteMoviesList = mMovieRepository.getFavoriteMovies();
    }

    public LiveData<List<MovieList>> getAllLists() {
        return mMovieLists;
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return mFavoriteMoviesList;
    }

    public void deleteMovieList(MovieList movieList) {
        mMovieListRepository.deleteMovieList(movieList);
    }
}
