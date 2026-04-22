package com.example.steamapp;

import android.app.AlertDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StoreFragment extends Fragment {

    private RecyclerView rvStore;
    private GameAdapter adapter;
    private List<Game> gameList;
    private String username;
    private DatabaseHelper dbHelper;

    public static StoreFragment newInstance(String username) {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        args.putString("USERNAME", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString("USERNAME");
        }
        dbHelper = new DatabaseHelper(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        ImageView ivProfile = view.findViewById(R.id.ivStoreProfilePic);
        String savedImageUri = dbHelper.getImageUri(username);
        if (savedImageUri != null) {
            try {
                ivProfile.setImageURI(Uri.parse(savedImageUri));
            } catch (Exception e) {
                ivProfile.setImageResource(android.R.drawable.sym_def_app_icon);
            }
        }

        ivProfile.setOnClickListener(v -> {
            if (getActivity() != null) {
                BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_navigation);
                bottomNav.setSelectedItemId(R.id.navigation_account);
            }
        });

        rvStore = view.findViewById(R.id.rvStore);
        rvStore.setLayoutManager(new LinearLayoutManager(getContext()));

        loadGames();

        adapter = new GameAdapter(gameList, this::showGameDetails);
        rvStore.setAdapter(adapter);

        EditText etSearch = view.findViewById(R.id.etSearchStore);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterGames(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void filterGames(String text) {
        List<Game> filteredList = new ArrayList<>();
        for (Game game : gameList) {
            if (game.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(game);
            }
        }
        adapter.updateList(filteredList);
    }

    private void loadGames() {
        gameList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllGames();
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("base_price"));
                int discount = cursor.getInt(cursor.getColumnIndexOrThrow("discount"));
                gameList.add(new Game(name, price, discount, getGameImage(name)));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private int getGameImage(String name) {
        switch (name) {
            case "Call of Duty: Jandouba Warfare": return R.drawable.callofdutyjandoopa;
            case "Escape from Bow Salem": return R.drawable.bowsalem;
            case "Better call Zaqlawi": return R.drawable.zaqlawi;
            case "Sfax Simulator:Africans Edition": return R.drawable.sfax;
            case "Grand Theft auto 6:djerba city": return R.drawable.djerba;
            case "Half Life 2:Episode Gafsa": return R.drawable.halflifegafsa;
            case "Counter Strike :Potato": return R.drawable.cspotato;
            case "Tita Fortress 2": return R.drawable.titafortress;
            case "Five nights At Epstein": return R.drawable.fivenightsatepstein;
            case "Elden cockRing": return R.drawable.elder;
            case "Batman Kairawan city": return R.drawable.batman;
            case "Attack on Jbenyana : Mathloothi Revenge": return R.drawable.attackonjbejayana;
            default: return R.mipmap.ic_launcher;
        }
    }

    private void showGameDetails(Game game) {
        View detailsView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_game_details, null);
        
        ImageView ivPoster = detailsView.findViewById(R.id.ivDetailPoster);
        TextView tvName = detailsView.findViewById(R.id.tvDetailName);
        TextView tvPrice = detailsView.findViewById(R.id.tvDetailPrice);
        RecyclerView rvReviews = detailsView.findViewById(R.id.rvReviews);
        RatingBar rbUser = detailsView.findViewById(R.id.rbUserRating);
        EditText etReview = detailsView.findViewById(R.id.etUserReview);
        View btnSubmit = detailsView.findViewById(R.id.btnSubmitReview);
        View btnBuy = detailsView.findViewById(R.id.btnDetailBuy);

        ivPoster.setImageResource(game.getImageResource());
        tvName.setText(game.getName());
        tvPrice.setText(game.getFormattedPrice());

        rvReviews.setLayoutManager(new LinearLayoutManager(getContext()));
        loadReviews(game.getName(), rvReviews);

        btnSubmit.setOnClickListener(v -> {
            float rating = rbUser.getRating();
            String review = etReview.getText().toString().trim();
            if (review.isEmpty()) {
                Toast.makeText(getContext(), "Please write a review", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.addReview(username, game.getName(), rating, review);
                etReview.setText("");
                rbUser.setRating(0);
                loadReviews(game.getName(), rvReviews);
                Toast.makeText(getContext(), "Review submitted!", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen)
                .setView(detailsView)
                .create();

        btnBuy.setOnClickListener(v -> {
            dialog.dismiss();
            showPurchaseConfirmation(game);
        });

        detailsView.findViewById(R.id.btnDetailBack).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void loadReviews(String gameName, RecyclerView rv) {
        List<Review> reviews = new ArrayList<>();
        Cursor cursor = dbHelper.getReviewsForGame(gameName);
        if (cursor.moveToFirst()) {
            do {
                String user = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                float rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));
                String text = cursor.getString(cursor.getColumnIndexOrThrow("review_text"));
                reviews.add(new Review(user, rating, text));
            } while (cursor.moveToNext());
        }
        cursor.close();
        rv.setAdapter(new ReviewAdapter(reviews));
    }

    private void showPurchaseConfirmation(Game game) {
        View quantityView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_quantity, null);
        EditText etQuantity = quantityView.findViewById(R.id.etQuantity);

        new AlertDialog.Builder(getContext())
                .setTitle("Confirm Purchase")
                .setView(quantityView)
                .setMessage("Do you want to purchase " + game.getName() + " for " + game.getFormattedPrice() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    String quantityStr = etQuantity.getText().toString().trim();
                    int quantity = 1;
                    if (!quantityStr.isEmpty()) {
                        try {
                            quantity = Integer.parseInt(quantityStr);
                        } catch (NumberFormatException e) {
                            quantity = 1;
                        }
                    }
                    if (quantity < 1) quantity = 1;

                    double totalPrice = game.getCurrentPrice() * quantity;
                    double currentBalance = dbHelper.getBalance(username);

                    if (currentBalance >= totalPrice) {
                        dbHelper.updateBalance(username, -totalPrice);
                        
                        StringBuilder keys = new StringBuilder();
                        for (int i = 0; i < quantity; i++) {
                            keys.append(generateRandomKey());
                            if (i < quantity - 1) keys.append("\n");
                        }

                        dbHelper.addPurchase(username, game.getName(), game.getFormattedPrice(), quantity, keys.toString());
                        showCDKeyDialog(keys.toString());
                    } else {
                        Toast.makeText(getContext(), "Insufficient funds in Steam Wallet!", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showCDKeyDialog(String key) {
        new AlertDialog.Builder(getContext())
                .setTitle("Purchase Successful")
                .setMessage("Your CD Key(s):\n" + key + "\n\nNote: The amount has been deducted from your Steam Wallet. Check History for details.")
                .setPositiveButton("OK", null)
                .show();
    }

    private String generateRandomKey() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            if (i < 2) sb.append("-");
        }
        return sb.toString();
    }
}
