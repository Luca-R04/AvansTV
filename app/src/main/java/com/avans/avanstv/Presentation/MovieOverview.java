package com.avans.avanstv.Presentation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.R;
import com.bumptech.glide.Glide;

public class MovieOverview extends Fragment {
    private final Movie mMovie;

    public MovieOverview(Movie mMovie) {
        this.mMovie = mMovie;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_overview, container, false);

        Button backArrow = view.findViewById(R.id.btn_back);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().remove(MovieOverview.this).commit();
            }
        });

        ImageView imageView = view.findViewById(R.id.movie_image_detail);

        Glide
                .with(this)
                .load("https://image.tmdb.org/t/p/original/" + mMovie.getPoster_path())
                .centerCrop()
                .into(imageView);

        // Inflate the layout for this fragment
        return view;
    }
}