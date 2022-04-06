package com.avans.avanstv.Presentation.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avans.avanstv.Data.MovieListRepository;
import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Cast;
import com.avans.avanstv.Domain.MovieList;

import java.util.List;

public class CastViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Cast>> mCastList = new MutableLiveData<>();

    public CastViewModel(@NonNull Application application) {
        super(application);
        MovieRepository mMovieRepository = MovieRepository.getInstance(application);
        mCastList.setValue(mMovieRepository.getCastList());
    }

    public MutableLiveData<List<Cast>> getAllCast() {
        return mCastList;
    }
}
