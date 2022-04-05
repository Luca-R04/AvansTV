package com.avans.avanstv.Presentation;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.avanstv.Data.MovieRepository;
import com.avans.avanstv.Domain.Genre;
import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class MovieExploreAdapter extends RecyclerView.Adapter<MovieExploreAdapter.MovieRecyclerAdapter>{
    private List<Movie> mMovieList;
    private Context mContext;

    public void setMovies(List<Movie> movieList) {
        this.mMovieList = movieList;
        notifyDataSetChanged();
    }

    public MovieExploreAdapter(Context context, List<Movie> movies) {
        this.mContext = context;
        this.mMovieList = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieRecyclerAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        if(viewType == R.layout.explore_recycler_movie){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_recycler_movie, parent, false);
        }

        else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_button, parent, false);
        }

        return new MovieRecyclerAdapter(itemView);

//        return new MovieRecyclerAdapter(LayoutInflater.from(mContext).inflate(R.layout.explore_recycler_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieRecyclerAdapter holder, int position) {

        if(position == mMovieList.size()) {
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Loading more movies...", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Glide
                    .with(mContext)
                    .load("https://image.tmdb.org/t/p/original/" + mMovieList.get(holder.getAdapterPosition()).getPoster_path())
                    .into(holder.imageView);

            holder.movieTitle.setText(mMovieList.get(holder.getAdapterPosition()).getTitle());

            StringBuilder dateMovie = new StringBuilder();
            String[] splitDate = mMovieList.get(holder.getAdapterPosition()).getRelease_date().split("-");
            dateMovie.append(splitDate[2]).append("-").append(splitDate[1]).append("-").append(splitDate[0]);

            holder.movieDate.setText(dateMovie);
            holder.movieRating.setText(String.valueOf(mMovieList.get(holder.getAdapterPosition()).getVote_average()));

            holder.itemView.setOnClickListener(view -> {
                Log.i("MovieAdapter", mMovieList.get(holder.getAdapterPosition()).getTitle());
                Intent intent = new Intent(mContext, MovieDetailsActivity.class);
                intent.putExtra("Movie", mMovieList.get(holder.getAdapterPosition()));
                mContext.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mMovieList != null) {
            return mMovieList.size() + 1;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mMovieList.size()) ? R.layout.load_button : R.layout.explore_recycler_movie;
    }

    static class MovieRecyclerAdapter extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView movieTitle, movieDate, movieRating;
        private final Button button;

        public MovieRecyclerAdapter(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_explore_movie);
            movieTitle = itemView.findViewById(R.id.explore_title);
            movieDate = itemView.findViewById(R.id.explore_date);
            movieRating = itemView.findViewById(R.id.explore_rating);
            button = (Button) itemView.findViewById(R.id.more_movies_btn);
        }
    }
}
