package com.avans.avanstv.Presentation.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.avans.avanstv.Data.MovieListRepository;
import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Movie;

import java.util.ArrayList;
import java.util.List;

public class PopularMovieViewModel extends AndroidViewModel {
    private static volatile PopularMovieViewModel INSTANCE;
    private LiveData<List<Movie>> mMovie;

    public PopularMovieViewModel(@NonNull Application application) {
        super(application);
        mMovie = MovieRepository.getLiveDataMovies();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return mMovie;
    }

    public void setmMovie(Movie movie) {
        List<Movie> updatetMovies = (List<Movie>) mMovie;
        for (Movie movieItem : updatetMovies) {
            if (movie.getMovieId() == movieItem.getMovieId()) {
                movieItem.setPersonalRating(movie.getPersonalRating());
            }
        }
        mMovie = (LiveData<List<Movie>>) updatetMovies;
    }

    public static PopularMovieViewModel getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new PopularMovieViewModel(application);
        }
        return INSTANCE;
    }
}
