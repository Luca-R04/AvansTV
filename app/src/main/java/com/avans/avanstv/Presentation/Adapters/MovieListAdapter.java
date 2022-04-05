package com.avans.avanstv.Presentation.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Domain.MovieList;
import com.avans.avanstv.R;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieRecyclerAdapter>{
    private List<MovieList> mMovieLists;
    private final Context mContext;

    public void setMovieLists(List<MovieList> movieLists) {
        this.mMovieLists = movieLists;
        notifyDataSetChanged();
    }

    public MovieListAdapter(Context context, List<MovieList> moviesLists) {
        this.mContext = context;
        this.mMovieLists = moviesLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieListAdapter.MovieRecyclerAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieRecyclerAdapter(LayoutInflater.from(mContext).inflate(R.layout.favorites_recycler_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.MovieRecyclerAdapter holder, int position) {

        if (mMovieLists.size() < holder.getAdapterPosition()) {
            //Set values for text fields
            MovieList movieList = mMovieLists.get(holder.getAdapterPosition());
            List<Movie> movies = movieList.getMovies();

            holder.listName.setText(movieList.getName());

            if (movies != null) {
                if (movies.size() >= 3) {
                    holder.movie1.setText(movies.get(0).getTitle());
                    holder.movie2.setText(movies.get(1).getTitle());
                    holder.movie3.setText(movies.get(2).getTitle());
                } else if (movies.size() >= 2) {
                    holder.movie1.setText(movies.get(0).getTitle());
                    holder.movie2.setText(movies.get(1).getTitle());
                } else {
                    holder.movie1.setText(movies.get(0).getTitle());
                }
            } else {
                holder.listName.setText(R.string.no_list_available);
            }
        }
        }



    @Override
    public int getItemCount() {
        if (mMovieLists != null) {
            return mMovieLists.size() + 1;
        }
        return 0;
    }

    static class MovieRecyclerAdapter extends RecyclerView.ViewHolder {
        private final TextView listName, movie1, movie2, movie3;

        public MovieRecyclerAdapter(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_name);
            movie1 = itemView.findViewById(R.id.movie_1);
            movie2 = itemView.findViewById(R.id.movie_2);
            movie3 = itemView.findViewById(R.id.movie_3);
        }
    }
}
