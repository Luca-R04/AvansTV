package com.avans.avanstv.Presentation.Adapters;

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
import com.avans.avanstv.Presentation.MovieDetailsActivity;
import com.avans.avanstv.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieRecyclerAdapter> {
    private List<Movie> mMovieList;
    private final Context mContext;

    public MovieAdapter(Context mContext,@NonNull List<Movie> mMovieList) {
        this.mContext = mContext;
        this.mMovieList = mMovieList;
        notifyDataSetChanged();
    }

    public void setMovies(List<Movie> movieList) {
        this.mMovieList = movieList;
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
                .load("https://image.tmdb.org/t/p/original/" + mMovieList.get(holder.getAdapterPosition()).getPoster_path())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

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

    static class MovieRecyclerAdapter extends RecyclerView.ViewHolder{
        private final ImageView imageView;

        public MovieRecyclerAdapter(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recycler_movie_img);
        }
    }
}