package com.avans.avanstv.Presentation;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Genre;
import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.R;
import com.bumptech.glide.Glide;

public class MovieOverview extends Fragment {
    private final Movie mMovie;
    private MovieRepository movieRepository = MovieRepository.getInstance();

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

        //Find fields from layout.
        Button backArrow = view.findViewById(R.id.btn_back);
        CardView cardFavorites = view.findViewById(R.id.card_favorite);
        TextView movieTitle = view.findViewById(R.id.movie_title_detail);
        TextView movieRating = view.findViewById(R.id.movie_rating_detail);
        TextView movieGenres = view.findViewById(R.id.movie_genres_detail);
        TextView movieDescription = view.findViewById(R.id.movie_description_detail);




        //OnClickListener for the backArrow to close the fragment.
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().remove(MovieOverview.this).commit();
            }
        });

        //Set all the fields
        ImageView imageView = view.findViewById(R.id.movie_image_detail);
        Glide
                .with(this)
                .load("https://image.tmdb.org/t/p/original/" + mMovie.getBackdrop_path())
                .centerCrop()
                .into(imageView);

        movieTitle.setText(mMovie.getTitle());
        movieRating.setText(mMovie.getVote_average() + "");

//        TODO: FIX GENRES, DISPLAYED NU NULL
        int[] genreList = mMovie.getGenre_ids();
        Genre[] genreArray = movieRepository.getGenres();


        StringBuilder genres = new StringBuilder();

        if (genreList == null) {
            genres.append("No genres.");
        } else {
            genres.append("Genres: ");
            int i = 0;
            for (Integer genreInt : genreList) {
                for (Genre genreObj : genreArray) {
                    if (genreInt == genreObj.getId()) {
                        genres.append(genreObj.getName());
                    }
                }
                if (genreList.length - 1 != i) {
                    genres.append(", ");
                    i++;
                }
            }
            genres.append(".");
        }


        movieGenres.setText(genres);
        movieDescription.setText(mMovie.getOverview());




        // Inflate the layout for this fragment
        return view;
    }
}