package com.avans.avanstv.Presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Genre;
import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Presentation.Adapters.CastAdapter;
import com.avans.avanstv.Presentation.Adapters.MovieAdapter;
import com.avans.avanstv.Presentation.ViewModel.CastViewModel;
import com.avans.avanstv.Presentation.ViewModel.PopularMovieViewModel;
import com.avans.avanstv.Presentation.ViewModel.TopRatedMovieViewModel;
import com.avans.avanstv.R;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

public class MovieDetailsActivity extends YouTubeBaseActivity {
    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movie_overview);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("Movie")) {
                Movie movie = (Movie) intent.getSerializableExtra("Movie");

                MovieRepository movieRepository = MovieRepository.getInstance(getApplication());
                youTubePlayerView = findViewById(R.id.youtubePlayerView);
                ImageView thumbnailView = findViewById(R.id.thumbnail_view);
                ConstraintLayout constraintLayout = findViewById(R.id.detail_constraint);

                YouTubePlayer.OnInitializedListener onInitializedListener = new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        if (movie.getYoutubeVideo() != null) {
                            youTubePlayer.cueVideo(movie.getYoutubeVideo().getKey());
                        } else {
                            youTubePlayerView.setVisibility(View.INVISIBLE);
                            constraintLayout.setVisibility(View.VISIBLE);
                            thumbnailView.setBackgroundColor(getResources().getColor(R.color.primary));
                            Glide
                                    .with(MovieDetailsActivity.this)
                                    .load("https://image.tmdb.org/t/p/original/" + movie.getPoster_path())
                                    .into(thumbnailView);
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                        Toast.makeText(MovieDetailsActivity.this, "Failed to load trailer!", Toast.LENGTH_SHORT).show();
                    }
                };

                youTubePlayerView.initialize("AIzaSyBsl39DH02ica_dPRXJWQsiRtcUcmeWx2g", onInitializedListener);

                MaterialButton backButton = findViewById(R.id.btn_back);
                TextView movieTitle = findViewById(R.id.movie_title_detail);
                TextView movieRating = findViewById(R.id.movie_rating_detail);
                TextView movieDate = findViewById(R.id.movie_date_detail);
                TextView movieGenres = findViewById(R.id.movie_genres_detail);
                TextView movieDescription = findViewById(R.id.movie_description_detail);

                backButton.setOnClickListener(view -> {
                    Intent i = new Intent(this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                });

                movieTitle.setText(movie.getTitle());
                movieRating.setText(String.valueOf(movie.getVote_average()));

                StringBuilder dateMovie = new StringBuilder();
                String[] splitDate = movie.getRelease_date().split("-");
                dateMovie.append(splitDate[2]).append("-").append(splitDate[1]).append("-").append(splitDate[0]);
                movieDate.setText(dateMovie);

                List<Integer> genreList = movie.getGenre_ids();
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
                        if (genreList.size() - 1 != i) {
                            genres.append(", ");
                            i++;
                        }
                    }
                    genres.append(".");
                }

                movieGenres.setText(genres);
                movieDescription.setText(movie.getOverview());

//                CastViewModel mCastViewModel = ViewModelProviders.of(this).get(CastViewModel.class);
//
//                // Create a Recyclerview and adapter to display the movies
//                RecyclerView castRecyclerView = findViewById(R.id.cast_recyclerview);
//                CastAdapter castAdapter = new CastAdapter(this, movie.getCast());
//                castRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//                castRecyclerView.setAdapter(castAdapter);
//
//                //updates adapter
//                mCastViewModel.getAllCast().observe(this.getLifeCycleOwner(), castAdapter::setCastList);

                ImageButton favoriteButton = findViewById(R.id.card_favorite_ic);
                CardView favoriteWrapper = findViewById(R.id.card_favorite);

                if (movie.isFavorite()) {
                    favoriteButton.setBackgroundColor(getResources().getColor(R.color.primary));
                    favoriteWrapper.setBackgroundColor(getResources().getColor(R.color.primary));
                }

                favoriteButton.setOnClickListener(view -> {
                    if (!movie.isFavorite()) {
                        movie.setFavorite(true);
                        favoriteButton.setBackgroundColor(getResources().getColor(R.color.primary));
                        favoriteWrapper.setBackgroundColor(getResources().getColor(R.color.primary));
                    } else {
                        movie.setFavorite(false);
                        favoriteButton.setBackgroundColor(getResources().getColor(R.color.white));
                        favoriteWrapper.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    movieRepository.setFavoriteMovie(movie);
                });
            }
        }
    }
}