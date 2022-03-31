package com.avans.avanstv.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.avans.avanstv.Presentation.ViewModel.PopularMovieViewModel;
import androidx.lifecycle.ViewModelProviders;
import com.avans.avanstv.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private PopularMovieViewModel mPopularMovieViewModel;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPopularMovieViewModel = ViewModelProviders.of(this).get(PopularMovieViewModel.class);

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
}