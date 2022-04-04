package com.avans.avanstv.Presentation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        return new MovieRecyclerAdapter(LayoutInflater.from(mContext).inflate(R.layout.featured_card_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieRecyclerAdapter holder, int position) {
        Glide
                .with(mContext)
                .load("https://image.tmdb.org/t/p/original/" + mMovieList.get(holder.getAdapterPosition()).getPoster_path())
                .into(holder.imageView);

        holder.movieTitle.setText(mMovieList.get(holder.getAdapterPosition()).getTitle());

        holder.itemView.setOnClickListener(view -> {
            Log.i("MovieAdapter", mMovieList.get(holder.getAdapterPosition()).getTitle());
            Intent intent = new Intent(mContext, MovieDetailsActivity.class);
            intent.putExtra("Movie", mMovieList.get(holder.getAdapterPosition()));
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (mMovieList != null) {
            return mMovieList.size();
        }
        return 0;
    }

    static class MovieRecyclerAdapter extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView movieTitle;

        public MovieRecyclerAdapter(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_featured_movie);
            movieTitle = itemView.findViewById(R.id.movie_title);
        }
    }
}
