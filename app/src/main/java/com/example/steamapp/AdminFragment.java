package com.example.steamapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminFragment extends Fragment {

    private RecyclerView rvAdminList;
    private DatabaseHelper dbHelper;
    private Button btnUsers, btnStore;
    private boolean isManagingUsers = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        dbHelper = new DatabaseHelper(getContext());
        rvAdminList = view.findViewById(R.id.rvAdminList);
        rvAdminList.setLayoutManager(new LinearLayoutManager(getContext()));
        
        btnUsers = view.findViewById(R.id.btnManageUsers);
        btnStore = view.findViewById(R.id.btnManageStore);

        btnUsers.setOnClickListener(v -> {
            isManagingUsers = true;
            loadUsers();
        });

        btnStore.setOnClickListener(v -> {
            isManagingUsers = false;
            loadStore();
        });

        loadUsers(); // Default
        return view;
    }

    private void loadUsers() {
        List<String> usernames = dbHelper.getAllUsernames();
        AdminUserAdapter adapter = new AdminUserAdapter(usernames, dbHelper, this::loadUsers);
        rvAdminList.setAdapter(adapter);
    }

    private void loadStore() {
        List<Game> games = new ArrayList<>();
        Cursor cursor = dbHelper.getAllGames();
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("base_price"));
                int discount = cursor.getInt(cursor.getColumnIndexOrThrow("discount"));
                games.add(new Game(name, price, discount, R.mipmap.ic_launcher));
            } while (cursor.moveToNext());
        }
        cursor.close();
        rvAdminList.setAdapter(new AdminGameAdapter(games, dbHelper, this::loadStore));
    }
}
