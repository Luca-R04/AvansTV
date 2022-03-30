package com.avans.avanstv.Presentation.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Movie;

import java.util.List;

public class PopularMovieViewModel extends AndroidViewModel {
    private LiveData<List<Movie>> mMovie;
    private MovieRepository mMovieRepository;

    public PopularMovieViewModel(@NonNull Application application) {
        super(application);
        mMovieRepository.getMovies();
    }

    public LiveData<List<Movie>> getMeals() {
        return mMovie;
    }
}
