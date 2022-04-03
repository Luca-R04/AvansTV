package com.avans.avanstv.Presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Genre;
import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

public class MovieDetailsActivity extends YouTubeBaseActivity {
    YouTubePlayer.OnInitializedListener onInitializedListener;
    YouTubePlayerView youTubePlayerView;

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

                onInitializedListener = new YouTubePlayer.OnInitializedListener(){
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        if (movie.getYoutubeVideo() != null) {
                            youTubePlayer.cueVideo(movie.getYoutubeVideo().getKey());
                        } else {
                            //TODO Laat thumbnail zien, omdat er geen trailer beschikbaar is.
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                };

                youTubePlayerView.initialize("AIzaSyBsl39DH02ica_dPRXJWQsiRtcUcmeWx2g", onInitializedListener);

                MaterialButton backButton = findViewById(R.id.btn_back);
                TextView movieTitle = findViewById(R.id.movie_title_detail);
                TextView movieRating = findViewById(R.id.movie_rating_detail);
                TextView movieGenres = findViewById(R.id.movie_genres_detail);
                TextView movieDescription = findViewById(R.id.movie_description_detail);

                backButton.setOnClickListener(view -> {
                    Intent i = new Intent(this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                });


                movieTitle.setText(movie.getTitle());
                movieRating.setText(String.valueOf(movie.getVote_average()));

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
            }
        }
    }
}