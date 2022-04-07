package com.avans.avanstv.Presentation.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avans.avanstv.Data.MovieListRepository;
import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Movie;

import java.util.ArrayList;
import java.util.List;

public class PopularMovieViewModel extends AndroidViewModel {
    private static volatile PopularMovieViewModel INSTANCE;
    private MutableLiveData<List<Movie>> mMovie;
    private MovieRepository movieRepository = new MovieRepository(getApplication());

    public PopularMovieViewModel(@NonNull Application application) {
        super(application);
        mMovie.setValue(MovieRepository.getLiveDataMovies().getValue());
    }

    public LiveData<List<Movie>> getAllMovies() {
        return mMovie;
    }

    public void setmMovie(Movie movie) {
        LiveData<List<Movie>> updatedMovies = mMovie;
        for (Movie movieItem : updatedMovies.getValue()) {
            if (movie.getMovieId() == movieItem.getMovieId()) {
                movieItem.setPersonalRating(movie.getPersonalRating());
            }
        }
        mMovie.setValue(updatedMovies.getValue());
    }

    public static PopularMovieViewModel getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new PopularMovieViewModel(application);
        }
        return INSTANCE;
    }

    public void getMoviesAsc() {
        new MovieRepository.getMoviesAsc().execute();
    }

    public void getMoviesDesc() {
        new MovieRepository.getMoviesDesc().execute();
    }

    public void getMoviesDateAsc() {
        new MovieRepository.getMoviesDateAsc().execute();
    }

    public void getMoviesDateDesc() {
        new MovieRepository.getMoviesDateDesc().execute();
    }

    public void getMoviesByGenre(int genreId) {
        new MovieRepository.getMoviesByGenre().execute(genreId);
    }
}
