package com.avans.avanstv.Presentation;

import androidx.appcompat.app.AppCompatActivity;
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
import com.avans.avanstv.Presentation.ViewModel.TopRatedMovieViewModel;
import com.avans.avanstv.R;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       //Bottom navigation//initiate controller for bottom navigation to witch between fragments
        BottomNavigationView bottomNavigationView = findViewById(R.id.btm_nav);
        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.host_fragment);
        final NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    public void setRandomMovie(List<Movie> movies) {
        if (movies != null) {
            int random = new Random().nextInt(movies.size());
            ImageView FeaturedMovieView = findViewById(R.id.img_randomMeal);

            Glide
                    .with(this)
                    .load("https://image.tmdb.org/t/p/original/" + movies.get(random).getPoster_path())
                    .into(FeaturedMovieView);
        } else {
            Log.d("HomeFragment", "The movie list is empty!");
        }

    }
}