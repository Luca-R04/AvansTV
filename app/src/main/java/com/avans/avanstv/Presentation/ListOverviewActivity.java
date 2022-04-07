package com.avans.avanstv.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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

                // Create a Recyclerview and adapter to display the movies
                RecyclerView listRecyclerView = findViewById(R.id.rv_list_overview);
                MovieAdapter movieAdapter = new MovieAdapter(this, movieList.getMovies());
                listRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                listRecyclerView.setAdapter(movieAdapter);

                TextView listName = findViewById(R.id.list_name_overview);
                listName.setText(movieList.getName());

                MaterialButton backButton = findViewById(R.id.list_btn_back);
                backButton.setOnClickListener(view -> onBackPressed());
            }
        }
    }
}