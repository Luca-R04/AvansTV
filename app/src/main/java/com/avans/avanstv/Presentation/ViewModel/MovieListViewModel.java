package com.avans.avanstv.Presentation.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.avans.avanstv.Data.MovieListRepository;
import com.avans.avanstv.Domain.MovieList;

import java.util.List;

public class MovieListViewModel extends AndroidViewModel {
    private final LiveData<List<MovieList>> mMovieLists;

    public MovieListViewModel(@NonNull Application application) {
        super(application);
        MovieListRepository mMovieRepository = MovieListRepository.getInstance(application);
        mMovieLists = mMovieRepository.getMovieLists();
    }

    public LiveData<List<MovieList>> getAllLists() {
        return mMovieLists;
    }
}
