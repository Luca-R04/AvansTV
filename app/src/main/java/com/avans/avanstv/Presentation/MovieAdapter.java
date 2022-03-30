package com.avans.avanstv.Presentation;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieRecyclerAdapter> {
    private List<Movie> movieList;
    private Context mContext;

    public void setMeal_list(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public MovieAdapter(Context mContext,@NonNull List<Movie> meal_list) {
        this.mContext = mContext;
        this.movieList = meal_list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieRecyclerAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieRecyclerAdapter(LayoutInflater.from(mContext).inflate(R.layout.recycler_movie, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieRecyclerAdapter holder, int position) {
        holder.textView.setText(movieList.get(position).getTitle());

        Glide
                .with(mContext)
                .load(movieList.get(position).getPoster_path())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.e("MealAdapter", movieList.get(position) + "");
                MovieOverview mealOverview = new MovieOverview(movieList.get(position));
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.host_fragment, mealOverview).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (movieList!= null) {
            return movieList.size();
        }
        return 0;
    }

    class MovieRecyclerAdapter extends RecyclerView.ViewHolder{
        private TextView textView;
        private ImageView imageView;

        public MovieRecyclerAdapter(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recycler_meals_img);
            textView = itemView.findViewById(R.id.recycler_meals_text);
        }
    }

}

