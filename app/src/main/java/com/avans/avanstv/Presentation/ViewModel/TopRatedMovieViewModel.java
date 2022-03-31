package com.avans.avanstv.Presentation.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Movie;

import java.util.List;

public class TopRatedMovieViewModel extends AndroidViewModel {
    private LiveData<List<Movie>> mMovies;
    private final MovieRepository mMovieRepository = MovieRepository.getInstance();

    public TopRatedMovieViewModel(@NonNull Application application) {
        super(application);
        mMovies = mMovieRepository.getLiveDataLatest();
    }

    public LiveData<List<Movie>> getLatestMovies() {
        return mMovies;
    }
}
