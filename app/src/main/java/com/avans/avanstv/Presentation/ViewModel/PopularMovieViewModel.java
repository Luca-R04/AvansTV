package com.avans.avanstv.Presentation.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.avans.avanstv.Data.MovieRepository;

public class PopularMovieViewModel extends AndroidViewModel {
    private MovieRepository mMovieRepository;

    public PopularMovieViewModel(@NonNull Application application) {
        super(application);

    }
}
