package com.example.steamapp;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class AdminGameAdapter extends RecyclerView.Adapter<AdminGameAdapter.GameViewHolder> {

    private List<Game> gameList;
    private DatabaseHelper dbHelper;
    private Runnable onUpdate;

    public AdminGameAdapter(List<Game> gameList, DatabaseHelper dbHelper, Runnable onUpdate) {
        this.gameList = gameList;
        this.dbHelper = dbHelper;
        this.onUpdate = onUpdate;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_admin, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = gameList.get(position);
        holder.tvName.setText(game.getName());
        holder.tvPrice.setText(String.format(Locale.getDefault(), "Base: $%.2f | Discount: %d%%", game.getBasePrice(), game.getDiscount()));

        holder.btnSetDiscount.setText("Change Discount");
        holder.btnDelete.setVisibility(View.GONE);
        holder.btnHistory.setVisibility(View.GONE);

        holder.btnSetDiscount.setOnClickListener(v -> showDiscountDialog(v, game));
    }

    private void showDiscountDialog(View v, Game game) {
        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_admin_discount, null);
        TextView tvTitle = view.findViewById(R.id.tvAdminGameName);
        EditText etDiscount = view.findViewById(R.id.etAdminDiscount);

        tvTitle.setText("Set Discount for: " + game.getName());
        etDiscount.setText(String.valueOf(game.getDiscount()));

        new AlertDialog.Builder(v.getContext())
                .setTitle("Manage Store")
                .setView(view)
                .setPositiveButton("Save", (dialog, which) -> {
                    String dStr = etDiscount.getText().toString().trim();
                    if (!dStr.isEmpty()) {
                        try {
                            int discount = Integer.parseInt(dStr);
                            if (discount >= 0 && discount <= 100) {
                                dbHelper.setGameDiscount(game.getName(), discount);
                                onUpdate.run();
                            }
                        } catch (NumberFormatException e) {}
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        Button btnSetDiscount, btnDelete, btnHistory;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvAdminUsername);
            tvPrice = itemView.findViewById(R.id.tvAdminBalance);
            btnSetDiscount = itemView.findViewById(R.id.btnAddMoney);
            btnDelete = itemView.findViewById(R.id.btnDeleteUser);
            btnHistory = itemView.findViewById(R.id.btnViewUserHistory);
        }
    }
}
