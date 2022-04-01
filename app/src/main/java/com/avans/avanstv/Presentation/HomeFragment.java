package com.avans.avanstv.Presentation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Genre;
import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Presentation.ViewModel.PopularMovieViewModel;
import com.avans.avanstv.Presentation.ViewModel.TopRatedMovieViewModel;
import com.avans.avanstv.R;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;


public class HomeFragment extends Fragment {
    private ImageView mFeaturedMovieView;
    private View homeView;
    private final MovieRepository movieRepository = MovieRepository.getInstance();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeView = inflater.inflate(R.layout.fragment_home, container, false);
        mFeaturedMovieView = homeView.findViewById(R.id.img_random_movie);

        PopularMovieViewModel mPopularMovieViewModel = ViewModelProviders.of(this).get(PopularMovieViewModel.class);
        TopRatedMovieViewModel mTopRatedMovieViewModel = ViewModelProviders.of(this).get(TopRatedMovieViewModel.class);

        // Create a Recyclerview and adapter to display the movies
        RecyclerView PopularRecyclerView = (RecyclerView) homeView.findViewById(R.id.rv_popular);
        MovieAdapter movieAdapter = new MovieAdapter(this.getContext(), mPopularMovieViewModel.getAllMovies().getValue());
        PopularRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        PopularRecyclerView.setAdapter(movieAdapter);

        mPopularMovieViewModel.getAllMovies().observe(this.getViewLifecycleOwner(), movies -> {
            movieAdapter.setMovies(movies); //updates adapter
            setRandomMovie(movies);
            Toast.makeText(this.getContext(), "Loaded: " + movies.size() + " movies", Toast.LENGTH_SHORT).show();
        });

        RecyclerView TopRatedRecyclerView = (RecyclerView) homeView.findViewById(R.id.rv_TopRated);
        MovieAdapter TopRatedMovieAdapter = new MovieAdapter(this.getContext(), mTopRatedMovieViewModel.getLatestMovies().getValue());
        TopRatedRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        TopRatedRecyclerView.setAdapter(TopRatedMovieAdapter);

        mTopRatedMovieViewModel.getLatestMovies().observe(this.getViewLifecycleOwner(), movies -> {
            TopRatedMovieAdapter.setMovies(movies); //updates adapter
            Toast.makeText(this.getContext(), "Loaded: " + movies.size() + " movies", Toast.LENGTH_SHORT).show();
        });

        // Inflate the layout for this fragment
        return homeView;
    }

    public void setRandomMovie(List<Movie> movies) {
        if (movies != null) {
            int random = new Random().nextInt(movies.size());
            Movie featuredMovie = movies.get(random);

            TextView featuredTitle = homeView.findViewById(R.id.featured_title_id);
            TextView featuredDate = homeView.findViewById(R.id.featured_date_id);
            TextView featuredLanguage = homeView.findViewById(R.id.featured_language_id);
            TextView featuredGenres = homeView.findViewById(R.id.featured_genres_id);

            featuredTitle.setText(featuredMovie.getTitle());
            featuredDate.setText(featuredMovie.getRelease_date());
            String languageCaps = featuredMovie.getOriginal_language().substring(0, 1).toUpperCase() + featuredMovie.getOriginal_language().substring(1).toLowerCase();
            featuredLanguage.setText(languageCaps);
            int[] genreList = featuredMovie.getGenre_ids();
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

            featuredGenres.setText(genres);

            Glide
                    .with(this)
                    .load("https://image.tmdb.org/t/p/original/" + featuredMovie.getPoster_path())
                    .into(mFeaturedMovieView);

            //Featured movie OnClickListener
            //Transfers movie info to MovieOverview
            CardView cardView = homeView.findViewById(R.id.card_randomMovie);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MovieOverview movieOverview = new MovieOverview(featuredMovie);
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.host_fragment, movieOverview).commitNowAllowingStateLoss();
                }
            });

        } else {
            Log.d("HomeFragment", "The movie list is empty!");
        }
    }


}