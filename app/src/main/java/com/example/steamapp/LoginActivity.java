package com.example.steamapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegisterLink, tvForgotPassword;
    private CheckBox cbRememberMe;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("SteamPrefs", MODE_PRIVATE);

        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);

        // Check if "Remember Me" was checked before
        boolean isRemembered = sharedPreferences.getBoolean("remember", false);
        if (isRemembered) {
            etUsername.setText(sharedPreferences.getString("username", ""));
            cbRememberMe.setChecked(true);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (dbHelper.checkUser(username, password)) {
                        // Handle Remember Me
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (cbRememberMe.isChecked()) {
                            editor.putBoolean("remember", true);
                            editor.putString("username", username);
                        } else {
                            editor.putBoolean("remember", false);
                            editor.putString("username", "");
                        }
                        editor.apply();

                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("USERNAME", username);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Please contact Steam Support to recover your account.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
