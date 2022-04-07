package com.avans.avanstv.Presentation;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Presentation.Adapters.MovieListAdapter;
import com.avans.avanstv.Presentation.ViewModel.MovieListViewModel;
import com.avans.avanstv.R;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    final List<ImageButton> mImageButtons = new ArrayList<>();

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
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        favoritesRecyclerView.setAdapter(movieAdapter);

        //updates adapter
        movieListViewModel.getAllLists().observe(getViewLifecycleOwner(), movieAdapter::setMovieLists);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                movieListViewModel.deleteMovieList(movieListViewModel.getAllLists().getValue().get(position));
                movieAdapter.removeMovieList(movieListViewModel.getAllLists().getValue().get(position));
                movieAdapter.notifyItemRemoved(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(favoritesRecyclerView);

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
        // i<3 to prevent out of bounds exception
        for (int i = 0; i < movies.size(); i++) {
            if (!movies.isEmpty() && i < 3) {
                Glide
                        .with(this.getActivity())
                        .load("https://image.tmdb.org/t/p/original/" + movies.get(i).getPoster_path())
                        .into(mImageButtons.get(i));
            }
        }
    }
}