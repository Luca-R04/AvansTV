package com.avans.avanstv.Presentation.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Movie;

import java.util.List;

public class SearchMovieViewModel extends AndroidViewModel {
    private final LiveData<List<Movie>> mMovie;

    public SearchMovieViewModel(@NonNull Application application) {
        super(application);
        MovieRepository mMovieRepository = MovieRepository.getInstance(application);
        mMovie = mMovieRepository.getSearchResults();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return mMovie;
    }
}
