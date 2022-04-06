package com.avans.avanstv.Presentation.Adapters;

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
import com.avans.avanstv.Domain.Cast;
import com.avans.avanstv.Domain.Movie;
import com.avans.avanstv.Presentation.MovieDetailsActivity;
import com.avans.avanstv.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastRecyclerAdapter> {
    private List<Cast> mCastList;
    private final Context mContext;

    public void setCastList(List<Cast> castList) {
        this.mCastList = castList;
        notifyDataSetChanged();
    }

    public CastAdapter(Context context, List<Cast> cast) {
        this.mContext = context;
        this.mCastList = cast;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CastRecyclerAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CastAdapter.CastRecyclerAdapter(LayoutInflater.from(mContext).inflate(R.layout.detail_recycler_cast, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CastRecyclerAdapter holder, int position) {
        Glide
                .with(mContext)
                .load("https://image.tmdb.org/t/p/w138_and_h175_face/" + mCastList.get(holder.getAdapterPosition()).getProfile_path())
                .into(holder.imageView);

        holder.castName.setText(mCastList.get(holder.getAdapterPosition()).getName());
    }

    @Override
    public int getItemCount() {
        if (mCastList != null) {
            return mCastList.size();
        }
        return 0;
    }

    static class CastRecyclerAdapter extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView castName;

        public CastRecyclerAdapter(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recycler_cast_img);
            castName = itemView.findViewById(R.id.cast_card_name);
        }
    }
}