package com.example.steamapp;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<Game> gameList;
    private OnGameClickListener listener;

    public interface OnGameClickListener {
        void onGameClick(Game game);
    }

    public GameAdapter(List<Game> gameList, OnGameClickListener listener) {
        this.gameList = gameList;
        this.listener = listener;
    }

    public void updateList(List<Game> newList) {
        this.gameList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = gameList.get(position);
        holder.tvGameName.setText(game.getName());
        holder.ivGameImage.setImageResource(game.getImageResource());

        if (game.getDiscount() > 0) {
            holder.tvDiscountTag.setVisibility(View.VISIBLE);
            holder.tvDiscountTag.setText("-" + game.getDiscount() + "%");
            
            holder.tvOldPrice.setVisibility(View.VISIBLE);
            holder.tvOldPrice.setText(String.format(Locale.getDefault(), "$%.2f", game.getBasePrice()));
            holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            
            holder.tvGamePrice.setText(game.getFormattedPrice());
        } else {
            holder.tvDiscountTag.setVisibility(View.GONE);
            holder.tvOldPrice.setVisibility(View.GONE);
            holder.tvGamePrice.setText(game.getFormattedPrice());
        }

        holder.itemView.setOnClickListener(v -> listener.onGameClick(game));
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGameImage;
        TextView tvGameName, tvGamePrice, tvOldPrice, tvDiscountTag;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGameImage = itemView.findViewById(R.id.ivGameImage);
            tvGameName = itemView.findViewById(R.id.tvGameName);
            tvGamePrice = itemView.findViewById(R.id.tvGamePrice);
            tvOldPrice = itemView.findViewById(R.id.tvOldPrice);
            tvDiscountTag = itemView.findViewById(R.id.tvDiscountTag);
        }
    }
}
