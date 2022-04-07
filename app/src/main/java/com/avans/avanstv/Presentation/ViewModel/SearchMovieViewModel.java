package com.avans.avanstv.Presentation.ViewModel;

import static com.avans.avanstv.Data.MovieRepository.getLiveDataCategoryMovies;

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
    private LiveData<List<Movie>> mMovieCategory;
    private static volatile PopularMovieViewModel INSTANCE;
    private final MovieRepository mMovieRepository;

    public void setMovie(String query) {
        this.mMovie.setValue(mMovieRepository.searchMovie(query));
    }

    public SearchMovieViewModel(@NonNull Application application) {
        super(application);
        mMovieRepository = MovieRepository.getInstance(application);
        mMovieCategory = getLiveDataCategoryMovies();
    }


    public LiveData<List<Movie>> getAllMovies() {
        return mMovie;
    }

    public LiveData<List<Movie>> getMovieCategory() {
        return mMovieCategory;
    }

    public static PopularMovieViewModel getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new PopularMovieViewModel(application);
        }
        return INSTANCE;
    }

    public void setMovies(List<Movie> movies) {

    }

    public void getMoviesAsc() {
        new MovieRepository.getMoviesAsc().execute();
    }

    public void getMoviesDesc() {
        new MovieRepository.getMoviesDesc().execute();
    }

    public void getMoviesDateAsc() {
        new MovieRepository.getMoviesDateAsc().execute();
    }

    public void getMoviesDateDesc() {
        new MovieRepository.getMoviesDateDesc().execute();
    }

    public void getMoviesByGenre(int genreId) {
        new MovieRepository.getMoviesByGenre().execute(genreId);
    }
}
