package com.avans.avanstv.Presentation;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.avans.avanstv.Data.MovieListRepository;
import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Cast;
import com.avans.avanstv.Domain.Genre;
import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Domain.MovieList;
import com.avans.avanstv.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends YouTubeBaseActivity {
    private YouTubePlayerView youTubePlayerView;
    private final static String TAG = MovieDetailsActivity.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movie_overview);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("Movie")) {
                Movie movie = (Movie) intent.getSerializableExtra("Movie");

                MovieRepository movieRepository = MovieRepository.getInstance(getApplication());
                MovieListRepository movieListRepository = MovieListRepository.getInstance(getApplication());
                youTubePlayerView = findViewById(R.id.youtubePlayerView);
                ImageView thumbnailView = findViewById(R.id.thumbnail_view);
                ConstraintLayout constraintLayout = findViewById(R.id.detail_constraint);

                YouTubePlayer.OnInitializedListener onInitializedListener = new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        if (movie.getYoutubeVideo() != null && movieRepository.hasInternet()) {
                            youTubePlayer.cueVideo(movie.getYoutubeVideo().getKey());
                        } else {
                            youTubePlayerView.setVisibility(View.INVISIBLE);
                            constraintLayout.setVisibility(View.VISIBLE);
                            thumbnailView.setBackgroundColor(getResources().getColor(R.color.primary));
                            Glide
                                    .with(MovieDetailsActivity.this)
                                    .load("https://image.tmdb.org/t/p/original/" + movie.getPoster_path())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
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
                TextView movieCast1 = findViewById(R.id.cast_name_card1);
                TextView movieCast2 = findViewById(R.id.cast_name_card2);
                TextView movieCast3 = findViewById(R.id.cast_name_card3);
                ImageView imageCast1 = findViewById(R.id.cast_img_1);
                ImageView imageCast2 = findViewById(R.id.cast_img_2);
                ImageView imageCast3 = findViewById(R.id.cast_img_3);
                RatingBar personalRatingBar = findViewById(R.id.personal_rating_bar);

                List<TextView> castTextViews = new ArrayList<>();
                castTextViews.add(movieCast1);
                castTextViews.add(movieCast2);
                castTextViews.add(movieCast3);

                List<ImageView> castImageView = new ArrayList<>();
                castImageView.add(imageCast1);
                castImageView.add(imageCast2);
                castImageView.add(imageCast3);

                backButton.setOnClickListener(view -> onBackPressed());

                movieTitle.setText(movie.getTitle());
                movieRating.setText(String.valueOf(movie.getVote_average()));

                StringBuilder dateMovie = new StringBuilder();
                if (movie.getRelease_date().split("-").length == 3) {
                    String[] splitDate = movie.getRelease_date().split("-");
                    dateMovie.append(splitDate[2]).append("-").append(splitDate[1]).append("-").append(splitDate[0]);
                } else {
                    dateMovie.append(movie.getRelease_date());
                }
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


                Log.d(TAG, "Retrieve cast for specific movie in Moviedetails");
                List<Cast> castList = movie.getCast();
                Log.d(TAG, "Retrieved cast for specific movie in Moviedetails");

                if (castList != null) {
                    for (int i = 0; i < 3; i++) {
                        Cast cast = castList.get(i);

                        Glide
                                .with(this)
                                .load("https://image.tmdb.org/t/p/w138_and_h175_face" + cast.getProfile_path())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(castImageView.get(i));

                        castTextViews.get(i).setText(cast.getName());
                    }
                } else {
                    Log.d(TAG, "Castlist is empty");
                }

                TextView listTitle = findViewById(R.id.add_list_title);
                Spinner spinner = findViewById(R.id.list_dropdown);
                Button addToListButton = findViewById(R.id.add_to_list);
                List<MovieList> movieLists = movieListRepository.getMovieLists().getValue();
                if (!movieLists.isEmpty()) {
                    ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    List<String> movieListNames = new ArrayList<>();
                    for (MovieList movieList : movieLists) {
                        movieListNames.add(movieList.getName());
                    }

                    for (String name : movieListNames) {
                        adapter.add(name);
                        adapter.notifyDataSetChanged();
                    }

                    final String[] spinnerValue = {""};

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            Object item = parent.getItemAtPosition(pos);
                            spinnerValue[0] = item.toString();
                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                    addToListButton.setOnClickListener(view -> {
                        movieListRepository.addMovieToList(spinnerValue[0], movie);
                        Toast.makeText(this, getString(R.string.added) + movie.getTitle() + getString(R.string.to) + spinnerValue[0], Toast.LENGTH_SHORT).show();
                        Log.i("MovieDetailsActivity", "Added " + movie.getTitle() + " to " + spinnerValue[0]);
                    });
                } else {
                    spinner.setVisibility(View.GONE);
                    addToListButton.setVisibility(View.GONE);
                    listTitle.setText(R.string.no_lists_available);
                }

                int personalRating = movie.getPersonalRating();
                personalRatingBar.setRating((float) personalRating);

                personalRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        int rating = (int) ratingBar.getRating();
                        Log.d(TAG, "Rating set to: " + rating);
                        movie.setPersonalRating(rating);
                    }
                });

                ImageButton favoriteButton = findViewById(R.id.card_favorite_ic);
                Icon filledHeart = Icon.createWithResource(this, R.drawable.ic_favorite).setTint(getColor(R.color.primary));
                Icon Heart = Icon.createWithResource(this, R.drawable.ic_favorite_border).setTint(getColor(R.color.primary));

                favoriteButton.setOnClickListener(view -> {
                    if (!movie.isFavorite()) {
                        movie.setFavorite(true);
                        favoriteButton.setImageIcon(filledHeart);
                    } else {
                        movie.setFavorite(false);
                        favoriteButton.setImageIcon(Heart);
                    }
                    Log.d(TAG, "Favorite after click: " + movie.isFavorite());
                    movieRepository.setFavoriteMovie(movie);
                });

                Log.d(TAG, "Favorite: " + movie.isFavorite());
                if (movie.isFavorite()) {
                    favoriteButton.setImageIcon(filledHeart);
                }
            }
        }
    }
}