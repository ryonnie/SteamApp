package com.example.steamapp;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView rvHistory;
    private String username;
    private DatabaseHelper dbHelper;

    public static HistoryFragment newInstance(String username) {
        HistoryFragment fragment = new HistoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        ImageView ivProfile = view.findViewById(R.id.ivHistoryProfilePic);
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

        rvHistory = view.findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        loadHistory();
        return view;
    }

    private void loadHistory() {
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
        rvHistory.setAdapter(adapter);
    }
}
