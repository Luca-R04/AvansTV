package com.avans.avanstv.Presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.avanstv.Data.MovieListRepository;
import com.avans.avanstv.Domain.MovieList;
import com.avans.avanstv.Presentation.Adapters.MovieAdapter;
import com.avans.avanstv.R;

import com.google.android.material.button.MaterialButton;

public class ListOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_overview);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("MovieList")) {
                MovieList movieList = (MovieList) intent.getSerializableExtra("MovieList");
                MovieListRepository movieListRepository = MovieListRepository.getInstance(getApplication());

                // Create a Recyclerview and adapter to display the movies
                RecyclerView listRecyclerView = findViewById(R.id.rv_list_overview);
                MovieAdapter movieAdapter = new MovieAdapter(this, movieList.getMovies());
                listRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                listRecyclerView.setAdapter(movieAdapter);

                ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        int position = viewHolder.getAdapterPosition();
                        Toast.makeText(ListOverviewActivity.this, getString(R.string.remove_movie_toast), Toast.LENGTH_SHORT).show();
                        movieListRepository.deleteMovieFromList(movieList.getMovies().get(position), movieList);
                        movieAdapter.removeMovie(movieList.getMovies().get(position));
                        movieAdapter.notifyItemRemoved(position);
                    }
                };

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
                itemTouchHelper.attachToRecyclerView(listRecyclerView);

                TextView listName = findViewById(R.id.list_name_overview);
                listName.setText(movieList.getName());

                MaterialButton backButton = findViewById(R.id.list_btn_back);
                backButton.setOnClickListener(view -> onBackPressed());
            }
        }
    }
}