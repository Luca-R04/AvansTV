package com.avans.avanstv.Presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.avans.avanstv.Data.MovieListRepository;
import com.avans.avanstv.Domain.MovieList;
import com.avans.avanstv.R;
import com.google.android.material.button.MaterialButton;

public class CreateListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_list_activity);

        MaterialButton backButton = findViewById(R.id.btn_back);
        Button createButton = findViewById(R.id.create_list_button);
        EditText listName = findViewById(R.id.list_name_input);

        backButton.setOnClickListener(view -> {
            onBackPressed();
        });

        createButton.setOnClickListener(view -> {
            if (!listName.getText().equals("")) {
                MovieList movieList = new MovieList(listName.getText().toString());
                MovieListRepository movieListRepository = MovieListRepository.getInstance(getApplication());
                movieListRepository.insertMovieList(movieList);

                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }
}