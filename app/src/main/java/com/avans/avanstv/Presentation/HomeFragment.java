package com.avans.avanstv.Presentation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Presentation.ViewModel.PopularMovieViewModel;
import com.avans.avanstv.R;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {
    private PopularMovieViewModel popularMovieViewModel;
//    private List<Movie> mMovie;
    private RecyclerView mPopularRecyclerView;
    private MovieAdapter movieAdapter;
//    MovieRepository movieRepository = MovieRepository.getInstance();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("HomeFragment", "OnCreate is called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);
//        mMovie = movieRepository.getMovies();
//        setRandomMovie();

//        //listener for change in the dataset in the LiveView
//        //updates the adapter when data is loaded
//        popularMovieViewModel.getMeals().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
//            @Override
//            public void onChanged(List<Movie> movies){
//                movieAdapter.setMeal_list(movies); //updates adapter
//                setRandomMovie(movies);
//                Toast.makeText(homeView.getContext(), "Loaded: " + movies.size() + " meals" ,Toast.LENGTH_SHORT).show();
//            }
//
//        });

        // Inflate the layout for this fragment
        return homeView;
    }

    public void setRandomMovie(List<Movie> movies) {
        if (movies != null) {
            int random = new Random().nextInt(movies.size());
            ImageView suggestedMealView = getActivity().findViewById(R.id.img_randomMeal);

            Glide
                    .with(getContext())
                    .load("https://image.tmdb.org" + movies.get(random).getPoster_path())
                    .centerCrop()
                    .into(suggestedMealView);
        } else {
            Log.d("HomeFragment", "The movie list is empty!");
        }

    }
}