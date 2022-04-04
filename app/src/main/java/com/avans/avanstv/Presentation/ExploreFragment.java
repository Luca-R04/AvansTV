package com.avans.avanstv.Presentation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avans.avanstv.Presentation.ViewModel.PopularMovieViewModel;
import com.avans.avanstv.R;

public class ExploreFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View exploreView = inflater.inflate(R.layout.fragment_explore, container, false);
        PopularMovieViewModel mPopularMovieViewModel = ViewModelProviders.of(this).get(PopularMovieViewModel.class);

        // Create a Recyclerview and adapter to display the movies
        RecyclerView exploreRecyclerView = exploreView.findViewById(R.id.rv_explore);
        MovieExploreAdapter movieAdapter = new MovieExploreAdapter(this.getContext(), mPopularMovieViewModel.getAllMovies().getValue());
        exploreRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        exploreRecyclerView.setAdapter(movieAdapter);

        mPopularMovieViewModel.getAllMovies().observe(this.getViewLifecycleOwner(), movies -> {
            movieAdapter.setMovies(movies); //updates adapter
        });

        return exploreView;
    }
}