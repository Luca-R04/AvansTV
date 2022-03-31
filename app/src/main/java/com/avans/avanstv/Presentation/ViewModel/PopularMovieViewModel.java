package com.avans.avanstv.Presentation.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Movie;

import java.util.List;

public class PopularMovieViewModel extends AndroidViewModel {
    private LiveData<List<Movie>> mMovie;
    private final MovieRepository mMovieRepository = new MovieRepository();

    public PopularMovieViewModel(@NonNull Application application) {
        super(application);
        mMovie = mMovieRepository.getLiveDataMovies();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return mMovie;
    }
}
