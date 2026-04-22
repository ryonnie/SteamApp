package com.example.steamapp;

import android.app.AlertDialog;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.UserViewHolder> {

    private List<String> userList;
    private DatabaseHelper dbHelper;
    private OnUserUpdateListener listener;

    public interface OnUserUpdateListener {
        void onUpdate();
    }

    public AdminUserAdapter(List<String> userList, DatabaseHelper dbHelper, OnUserUpdateListener listener) {
        this.userList = userList;
        this.dbHelper = dbHelper;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_admin, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        String username = userList.get(position);
        double balance = dbHelper.getBalance(username);

        holder.tvUsername.setText("User: " + username);
        holder.tvBalance.setText(String.format(Locale.getDefault(), "Balance: $%.2f", balance));

        holder.btnAddMoney.setOnClickListener(v -> {
            dbHelper.updateBalance(username, 500);
            notifyItemChanged(position);
            listener.onUpdate();
        });

        holder.btnDelete.setOnClickListener(v -> {
            dbHelper.deleteUser(username);
            userList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, userList.size());
            listener.onUpdate();
        });

        holder.btnViewHistory.setOnClickListener(v -> showUserHistory(v, username));
    }

    private void showUserHistory(View v, String username) {
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.fragment_history, null);
        
        // Show user's profile pic in the history dialog
        ImageView ivProfile = dialogView.findViewById(R.id.ivHistoryProfilePic);
        String savedImageUri = dbHelper.getImageUri(username);
        if (savedImageUri != null) {
            try {
                ivProfile.setImageURI(Uri.parse(savedImageUri));
            } catch (Exception e) {
                ivProfile.setImageResource(android.R.drawable.sym_def_app_icon);
            }
        }

        RecyclerView rv = dialogView.findViewById(R.id.rvHistory);
        rv.setLayoutManager(new LinearLayoutManager(v.getContext()));

        List<Purchase> purchases = new ArrayList<>();
        Cursor cursor = dbHelper.getPurchases(username);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("game_name"));
                String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                int qty = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                String keys = cursor.getString(cursor.getColumnIndexOrThrow("cd_keys"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("purchase_date"));
                purchases.add(new Purchase(name, price, qty, keys, date));
            } while (cursor.moveToNext());
        }
        cursor.close();

        PurchaseAdapter adapter = new PurchaseAdapter(purchases);
        rv.setAdapter(adapter);

        new AlertDialog.Builder(v.getContext())
                .setTitle("History: " + username)
                .setView(dialogView)
                .setPositiveButton("Close", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvBalance;
        Button btnAddMoney, btnDelete, btnViewHistory;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvAdminUsername);
            tvBalance = itemView.findViewById(R.id.tvAdminBalance);
            btnAddMoney = itemView.findViewById(R.id.btnAddMoney);
            btnDelete = itemView.findViewById(R.id.btnDeleteUser);
            btnViewHistory = itemView.findViewById(R.id.btnViewUserHistory);
        }
    }
}
