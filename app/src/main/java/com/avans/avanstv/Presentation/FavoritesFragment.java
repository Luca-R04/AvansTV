package com.avans.avanstv.Presentation;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avans.avanstv.Presentation.Adapters.MovieListAdapter;
import com.avans.avanstv.Presentation.ViewModel.MovieListViewModel;
import com.avans.avanstv.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FavoritesFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View favoriteView = inflater.inflate(R.layout.fragment_favorites, container, false);

        FloatingActionButton addListButton = favoriteView.findViewById(R.id.add_list_button);

        addListButton.setOnClickListener(view -> {
            Intent intent = new Intent(this.getContext(), CreateListActivity.class);
            startActivity(intent);
        });

        MovieListViewModel movieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);

        RecyclerView favoritesRecyclerView = favoriteView.findViewById(R.id.favorites_list_section1);
        MovieListAdapter movieAdapter = new MovieListAdapter(this.getContext(), movieListViewModel.getAllLists().getValue());
        favoritesRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        favoritesRecyclerView.setAdapter(movieAdapter);

        movieListViewModel.getAllLists().observe(this.getViewLifecycleOwner(), movies -> {
            movieAdapter.setMovieLists(movies); //updates adapter
        });

        // Inflate the layout for this fragment
        return favoriteView;
    }
}