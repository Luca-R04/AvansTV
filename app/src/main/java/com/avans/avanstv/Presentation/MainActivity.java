package com.avans.avanstv.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Presentation.ViewModel.PopularMovieViewModel;
import com.avans.avanstv.R;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private PopularMovieViewModel mPopularMovieViewModel;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPopularMovieViewModel = ViewModelProviders.of(this).get(PopularMovieViewModel.class);

        mPopularMovieViewModel.getAllMovies().observe(this, movies -> {
            adapter.setMovies(movies); //updates adapter
            setRandomMovie(movies);
            Toast.makeText(this, "Loaded: " + movies.size() + " movies" ,Toast.LENGTH_SHORT).show();
        });

        //Bottom navigation
        //initiate controller for bottom navigation to witch between fragments
        BottomNavigationView bottomNavigationView = findViewById(R.id.btm_nav);
        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.host_fragment);
        final NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Create a Recyclerview and adapter to display the meals
        RecyclerView PopularRecyclerView = (RecyclerView) findViewById(R.id.rv_popular);
        adapter = new MovieAdapter(this, mPopularMovieViewModel.getAllMovies().getValue());
        // Get column count to adjust to vertical or horizontal layout
        PopularRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        PopularRecyclerView.setAdapter(adapter);

    }

    public void setRandomMovie(List<Movie> movies) {
        if (movies != null) {
            int random = new Random().nextInt(movies.size());
            ImageView suggestedMealView = findViewById(R.id.img_randomMeal);

            Glide
                    .with(this)
                    .load("https://image.tmdb.org/t/p/original/" + movies.get(random).getPoster_path())
                    .centerCrop()
                    .into(suggestedMealView);
        } else {
            Log.d("HomeFragment", "The movie list is empty!");
        }

    }
}