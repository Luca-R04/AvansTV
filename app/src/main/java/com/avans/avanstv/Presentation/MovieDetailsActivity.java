package com.avans.avanstv.Presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.R;
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
                Button playButton = findViewById(R.id.play_button);

//                MovieRepository movieRepository = MovieRepository.getInstance();
//                movieRepository.setVideosFromApi(movie.getId());

                playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playVideo(movie.getYoutubeVideo().getKey(), youtubePlayerView);
                    }
                });
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