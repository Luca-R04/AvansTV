package com.avans.avanstv.Presentation.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Movie;

import java.util.List;

public class TopRatedMovieViewModel extends AndroidViewModel {
    private static volatile TopRatedMovieViewModel INSTANCE;
    private LiveData<List<Movie>> mMovies;

    public TopRatedMovieViewModel(@NonNull Application application) {
        super(application);
        MovieRepository mMovieRepository = MovieRepository.getInstance(application);
        mMovies = mMovieRepository.getTopRated();
    }

    public LiveData<List<Movie>> getLatestMovies() {
        return mMovies;
    }

    public void setmMovies(Movie movie) {
        LiveData<List<Movie>> updatetMovies = mMovies;
        for (Movie movieItem : updatetMovies.getValue()) {
            if (movie.getMovieId() == movieItem.getMovieId()) {
                movieItem.setPersonalRating(movie.getPersonalRating());
            }
        }
        mMovies = updatetMovies;
    }

    public static TopRatedMovieViewModel getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new TopRatedMovieViewModel(application);
        }
        return INSTANCE;
    }
}
