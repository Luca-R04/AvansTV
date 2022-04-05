package com.avans.avanstv.Presentation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Presentation.ViewModel.PopularMovieViewModel;
import com.avans.avanstv.R;

public class ExploreFragment extends Fragment {
    public static final String TAG = ExploreFragment.class.getSimpleName();

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
        exploreRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        exploreRecyclerView.setAdapter(movieAdapter);

        mPopularMovieViewModel.getAllMovies().observe(this.getViewLifecycleOwner(), movies -> {
            movieAdapter.setMovies(movies); //updates adapter
        });

        SearchView searchView = exploreView.findViewById(R.id.explore_search);
        MovieRepository movieRepository = MovieRepository.getInstance(getActivity().getApplication());
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint(getString(R.string.query_hint));

        //QueryTextListener on the searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                movieRepository.searchMovie(searchView.getQuery().toString());
                Log.d(TAG, "" + movieRepository.searchMovie(searchView.getQuery().toString()));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, "The textfield now says:" + searchView.getQuery().toString());
                return false;
            }
        });

        return exploreView;
    }
}