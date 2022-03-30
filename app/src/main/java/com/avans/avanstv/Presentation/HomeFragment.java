package com.avans.avanstv.Presentation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Presentation.ViewModel.PopularMovieViewModel;
import com.avans.avanstv.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {
    private PopularMovieViewModel popularMovieViewModel;
    private List<Movie> mMovie = new ArrayList<>();
    private RecyclerView mPopularRecyclerView;
    private MovieAdapter movieAdapter;

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
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);


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

    private void setRandomMovie(List<Movie> movies) {
        int random = new Random().nextInt(movies.size());
        ImageView suggestedMealView = getActivity().findViewById(R.id.img_randomMeal);

        Glide
                .with(getContext())
                .load("https://image.tmdb.org" + movies.get(random).getPoster_path())
                .centerCrop()
                .into(suggestedMealView);

    }
}