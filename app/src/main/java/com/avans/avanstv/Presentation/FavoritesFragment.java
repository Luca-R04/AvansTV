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
import android.widget.ImageButton;

import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Presentation.Adapters.MovieListAdapter;
import com.avans.avanstv.Presentation.ViewModel.MovieListViewModel;
import com.avans.avanstv.R;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    List<ImageButton> mImageButtons = new ArrayList<>();

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
        favoritesRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));
        favoritesRecyclerView.setAdapter(movieAdapter);

        //updates adapter
        movieListViewModel.getAllLists().observe(getViewLifecycleOwner(), movieAdapter::setMovieLists);

        ImageButton movie1 = favoriteView.findViewById(R.id.favorite_movie_1);
        ImageButton movie2 = favoriteView.findViewById(R.id.favorite_movie_2);
        ImageButton movie3 = favoriteView.findViewById(R.id.favorite_movie_3);
        mImageButtons.add(movie1);
        mImageButtons.add(movie2);
        mImageButtons.add(movie3);

        movieListViewModel.getFavoriteMovies().observe(getViewLifecycleOwner(), this::setFavoriteMovies);

        // Inflate the layout for this fragment
        return favoriteView;
    }

    private void setFavoriteMovies(List<Movie> movies) {
        for (int i = 0; i < 3; i++) {
            if (movies.get(i) != null) {
                Glide
                        .with(this.getActivity())
                        .load("https://image.tmdb.org/t/p/original/" + movies.get(i).getPoster_path())
                        .into(mImageButtons.get(i));
            }
        }
    }
}