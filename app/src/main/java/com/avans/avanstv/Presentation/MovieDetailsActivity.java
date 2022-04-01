package com.avans.avanstv.Presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Genre;
import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MovieDetailsActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movie_overview);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("Movie")) {
                Movie movie = (Movie) intent.getSerializableExtra("Movie");
                final YouTubePlayerView youtubePlayerView = findViewById(R.id.youtubePlayerView);

                MovieRepository movieRepository = MovieRepository.getInstance();
//                movieRepository.setVideosFromApi(movie.getId());

                Button playButton = findViewById(R.id.play_button);
                MaterialButton backButton = findViewById(R.id.btn_back);
                CardView cardFavorites = findViewById(R.id.card_favorite);
                TextView movieTitle = findViewById(R.id.movie_title_detail);
                TextView movieRating = findViewById(R.id.movie_rating_detail);
                TextView movieGenres = findViewById(R.id.movie_genres_detail);
                TextView movieDescription = findViewById(R.id.movie_description_detail);

                playButton.setOnClickListener(view ->
                        playVideo(movie.getYoutubeVideo().getKey(), youtubePlayerView)
                );

                backButton.setOnClickListener(view -> {
                    Intent i = new Intent(this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                });


                movieTitle.setText(movie.getTitle());
                movieRating.setText(movie.getVote_average() + "");

                movieGenres.setText(movie.getGenres() + "");
                int[] genreList = movie.getGenre_ids();
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
                movieDescription.setText(movie.getOverview());
            }
        }
    }

    public void playVideo(final String videoId, YouTubePlayerView youTubePlayerView) {
        //initialize youtube player view
        youTubePlayerView.initialize("AIzaSyBsl39DH02ica_dPRXJWQsiRtcUcmeWx2g",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.cueVideo(videoId);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                        Log.e("MovieOverview", "Failed to play YouTube video.");
                    }
                });
    }
}