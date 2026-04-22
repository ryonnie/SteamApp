package com.example.steamapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private String username;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = getIntent().getStringExtra("USERNAME");
        dbHelper = new DatabaseHelper(this);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        
        boolean isAdmin = dbHelper.isAdmin(username);

        // Configure menu based on role
        Menu menu = bottomNav.getMenu();
        if (isAdmin) {
            menu.findItem(R.id.navigation_admin).setVisible(true);
            menu.findItem(R.id.navigation_store).setVisible(false);
            menu.findItem(R.id.navigation_history).setVisible(false);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_store) {
                selectedFragment = StoreFragment.newInstance(username);
            } else if (itemId == R.id.navigation_history) {
                selectedFragment = HistoryFragment.newInstance(username);
            } else if (itemId == R.id.navigation_account) {
                selectedFragment = AccountFragment.newInstance(username);
            } else if (itemId == R.id.navigation_admin) {
                selectedFragment = new AdminFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // Set default fragment
        if (savedInstanceState == null) {
            Fragment defaultFragment;
            if (isAdmin) {
                defaultFragment = new AdminFragment();
                bottomNav.setSelectedItemId(R.id.navigation_admin);
            } else {
                defaultFragment = StoreFragment.newInstance(username);
                bottomNav.setSelectedItemId(R.id.navigation_store);
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, defaultFragment)
                    .commit();
        }
    }
}
