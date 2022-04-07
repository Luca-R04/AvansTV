package com.avans.avanstv.Presentation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;

import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Presentation.Adapters.MovieExploreAdapter;
import com.avans.avanstv.Presentation.ViewModel.PopularMovieViewModel;
import com.avans.avanstv.Presentation.ViewModel.SearchMovieViewModel;
import com.avans.avanstv.R;

public class ExploreFragment extends Fragment {
    public static final String TAG = ExploreFragment.class.getSimpleName();
    private SharedPreferences mSharedPreferences;
    private MovieRepository mMovieRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View exploreView = inflater.inflate(R.layout.fragment_explore, container, false);
        SearchMovieViewModel mSearchViewModel = ViewModelProviders.of(this).get(SearchMovieViewModel.class);
        mMovieRepository = MovieRepository.getInstance(requireActivity().getApplication());
        PopularMovieViewModel mPopularMovieViewModel = ViewModelProviders.of(this).get(PopularMovieViewModel.class);

        // Create a Recyclerview and adapter to display the movies
        RecyclerView exploreRecyclerView = exploreView.findViewById(R.id.rv_explore);
        MovieExploreAdapter movieAdapter = new MovieExploreAdapter(this.getContext(), mPopularMovieViewModel.getAllMovies().getValue());
        exploreRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        exploreRecyclerView.setAdapter(movieAdapter);

        SearchView searchView = exploreView.findViewById(R.id.explore_search);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint(getString(R.string.query_hint));
        searchView.setIconifiedByDefault(false);

        //ImageView Filter
        ImageView filterButton = exploreView.findViewById(R.id.filter_button);

        //OnClickListener
        filterButton.setOnClickListener(view -> Navigation.findNavController(exploreView).navigate(R.id.action_exploreFragment_to_filterContained));

        //QueryTextListener on the searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(exploreView.getWindowToken(), 0);

                Log.i(TAG, "onQueryTextSubmit");

                if (!searchView.getQuery().toString().equals("")) {
                    mMovieRepository.searchMovie(searchView.getQuery().toString());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, "The text field now says:" + s);
                return false;
            }
        });

        //updates adapter
        mSearchViewModel.getAllMovies().observe(getViewLifecycleOwner(), movieAdapter::setMovies);

        //Preferences filters
        loadPreference();

        return exploreView;
    }


    private void loadPreference() {
        //Set SharedPreferences
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        //Sub-methods
        SortTitle();
        SortDate();
        FilterGenre();
    }

    //Listener for to update the preference immediately;
    private void SortTitle() {
        String title = mSharedPreferences.getString("title", "");
        switch (title) {
            case "A-Z":
                mMovieRepository.getMoviesAsc();
                Log.d(TAG, "A-Z");
                break;
            case "Z-A":
                Log.d(TAG, "Z-A");
                mMovieRepository.getMoviesDesc();
                break;
            default:
                Log.d(TAG, "Default");
                break;
        }
    }

    private void SortDate() {
        String date = mSharedPreferences.getString("date", "");
            switch (date){
                case "Most recent":
                    Log.d(TAG, "Most recent");
                    mMovieRepository.getMoviesDateDesc();
                    break;
                case "Oldest":
                    Log.d(TAG, "Oldest");
                    mMovieRepository.getMoviesDateAsc();
                    break;
                default:
                    Log.d(TAG, "Default");
                    break;
        }
    }

    private void FilterGenre() {
        String genreIdString = mSharedPreferences.getString("genre", "").replace("@", "");
        Log.d(TAG, genreIdString + "");
        if (!genreIdString.equals("none") && !genreIdString.equals("")) {
            mMovieRepository.getMoviesByGenre(Integer.parseInt(genreIdString));
        }
    }
}