package com.avans.avanstv.Presentation.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Movie;

import java.util.List;

public class SearchMovieViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Movie>> mMovie = new MutableLiveData<>();
    private final MovieRepository mMovieRepository;

    public void setMovie(String query) {
        this.mMovie.setValue(mMovieRepository.searchMovie(query));
    }

    public SearchMovieViewModel(@NonNull Application application) {
        super(application);
        mMovieRepository = MovieRepository.getInstance(application);
    }


    public LiveData<List<Movie>> getAllMovies() {
        return mMovie;
    }
}
