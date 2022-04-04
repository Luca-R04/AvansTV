package com.avans.avanstv.Presentation;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieRecyclerAdapter> {
    private List<Movie> movieList;
    private final Context mContext;

    public void setMovies(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public MovieAdapter(Context mContext,@NonNull List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieRecyclerAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieRecyclerAdapter(LayoutInflater.from(mContext).inflate(R.layout.home_recycler_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieRecyclerAdapter holder, int position) {
        Glide
                .with(mContext)
                .load("https://image.tmdb.org/t/p/original/" + movieList.get(holder.getAdapterPosition()).getPoster_path())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(view -> {
            Log.i("MovieAdapter", movieList.get(holder.getAdapterPosition()).getTitle());
            Intent intent = new Intent(mContext, MovieDetailsActivity.class);
            intent.putExtra("Movie", movieList.get(holder.getAdapterPosition()));
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (movieList!= null) {
            return movieList.size();
        }
        return 0;
    }

    static class MovieRecyclerAdapter extends RecyclerView.ViewHolder{
        private final ImageView imageView;

        public MovieRecyclerAdapter(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recycler_movie_img);
        }
    }

}

