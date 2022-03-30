package com.avans.avanstv.Presentation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.R;

public class MovieOverview extends Fragment {
    private final Movie mMovie;

    public MovieOverview(Movie mMovie) {
        this.mMovie = mMovie;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_overview, container, false);

        // Inflate the layout for this fragment
        return view;
    }
}