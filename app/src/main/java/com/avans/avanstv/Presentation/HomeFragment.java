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
import android.widget.ImageView;
import android.widget.Toast;

import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Presentation.ViewModel.PopularMovieViewModel;
import com.avans.avanstv.Presentation.ViewModel.TopRatedMovieViewModel;
import com.avans.avanstv.R;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;
import java.util.Random;


public class HomeFragment extends Fragment {
    private ImageView mFeaturedMovieView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);
        mFeaturedMovieView = homeView.findViewById(R.id.img_random_movie);

        PopularMovieViewModel mPopularMovieViewModel = ViewModelProviders.of(this).get(PopularMovieViewModel.class);
        TopRatedMovieViewModel mTopRatedMovieViewModel = ViewModelProviders.of(this).get(TopRatedMovieViewModel.class);

        // Create a Recyclerview and adapter to display the movies
        RecyclerView PopularRecyclerView = (RecyclerView) homeView.findViewById(R.id.rv_popular);
        MovieAdapter movieAdapter = new MovieAdapter(this.getContext(), mPopularMovieViewModel.getAllMovies().getValue());
        PopularRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        PopularRecyclerView.setAdapter(movieAdapter);

        mPopularMovieViewModel.getAllMovies().observe(this.getViewLifecycleOwner(), movies -> {
            movieAdapter.setMovies(movies); //updates adapter
            setRandomMovie(movies);
            Toast.makeText(this.getContext(), "Loaded: " + movies.size() + " movies" ,Toast.LENGTH_SHORT).show();
        });

        RecyclerView TopRatedRecyclerView = (RecyclerView) homeView.findViewById(R.id.rv_TopRated);
        MovieAdapter TopRatedMovieAdapter = new MovieAdapter(this.getContext(), mTopRatedMovieViewModel.getLatestMovies().getValue());
        TopRatedRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        TopRatedRecyclerView.setAdapter(TopRatedMovieAdapter);

        mTopRatedMovieViewModel.getLatestMovies().observe(this.getViewLifecycleOwner(), movies -> {
            TopRatedMovieAdapter.setMovies(movies); //updates adapter
            Toast.makeText(this.getContext(), "Loaded: " + movies.size() + " movies" ,Toast.LENGTH_SHORT).show();
        });

        // Inflate the layout for this fragment
        return homeView;
    }

    public void setRandomMovie(List<Movie> movies) {
        if (movies != null) {
            int random = new Random().nextInt(movies.size());

            Glide
                    .with(this)
                    .load("https://image.tmdb.org/t/p/original/" + movies.get(random).getPoster_path())
                    .into(mFeaturedMovieView);
        } else {
            Log.d("HomeFragment", "The movie list is empty!");
        }
    }
}