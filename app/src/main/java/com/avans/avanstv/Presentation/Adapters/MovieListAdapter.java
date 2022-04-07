package com.avans.avanstv.Presentation.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.avanstv.Domain.MovieList;
import com.avans.avanstv.Presentation.ListOverviewActivity;
import com.avans.avanstv.R;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListRecyclerAdapter> {
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
    public MovieListRecyclerAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieListRecyclerAdapter(LayoutInflater.from(mContext).inflate(R.layout.favorites_recycler_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListRecyclerAdapter holder, int position) {
        MovieList movieList = mMovieLists.get(holder.getAdapterPosition());

        holder.listName.setText(movieList.getName());

        holder.itemView.setOnClickListener(view -> {
            Log.i("MovieAdapter", mMovieLists.get(holder.getAdapterPosition()).getName());
            Intent intent = new Intent(mContext, ListOverviewActivity.class);
            intent.putExtra("MovieList", mMovieLists.get(holder.getAdapterPosition()));
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (mMovieLists != null) {
            return mMovieLists.size();
        }
        return 0;
    }

    static class MovieListRecyclerAdapter extends RecyclerView.ViewHolder {
        private final TextView listName;

        public MovieListRecyclerAdapter(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_name);
        }
    }
}
