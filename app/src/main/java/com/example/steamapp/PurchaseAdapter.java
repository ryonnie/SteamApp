package com.example.steamapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder> {

    private List<Purchase> purchaseList;

    public PurchaseAdapter(List<Purchase> purchaseList) {
        this.purchaseList = purchaseList;
    }

    @NonNull
    @Override
    public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase, parent, false);
        return new PurchaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseViewHolder holder, int position) {
        Purchase purchase = purchaseList.get(position);
        holder.tvGameName.setText(purchase.getGameName());
        holder.tvDetails.setText("Qty: " + purchase.getQuantity() + " | Price: " + purchase.getPrice());
        holder.tvDate.setText("Date: " + purchase.getDate());
        holder.tvKeys.setText("Keys: " + purchase.getCdKeys());
    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    static class PurchaseViewHolder extends RecyclerView.ViewHolder {
        TextView tvGameName, tvDetails, tvDate, tvKeys;

        public PurchaseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGameName = itemView.findViewById(R.id.tvPurchaseGameName);
            tvDetails = itemView.findViewById(R.id.tvPurchaseDetails);
            tvDate = itemView.findViewById(R.id.tvPurchaseDate);
            tvKeys = itemView.findViewById(R.id.tvPurchaseKeys);
        }
    }
}
