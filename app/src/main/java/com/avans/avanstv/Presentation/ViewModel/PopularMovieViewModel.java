package com.avans.avanstv.Presentation.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Movie;

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

    public void setMovie(Movie movie) {
        LiveData<List<Movie>> updatedMovies = mMovie;
        for (Movie movieItem : updatedMovies.getValue()) {
            if (movie.getMovieId() == movieItem.getMovieId()) {
                movieItem.setPersonalRating(movie.getPersonalRating());
            }
        }
        mMovie = updatedMovies;
    }



    public static PopularMovieViewModel getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new PopularMovieViewModel(application);
        }
        return INSTANCE;
    }

}
