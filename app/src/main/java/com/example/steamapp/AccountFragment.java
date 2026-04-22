package com.example.steamapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class AccountFragment extends Fragment {

    private ImageView ivProfilePic;
    private TextView tvUsername, tvWalletBalance, tvChangePic;
    private Button btnLogout;
    private String username;
    private DatabaseHelper dbHelper;

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    ivProfilePic.setImageURI(uri);
                    dbHelper.updateImage(username, uri.toString());
                    
                    // Note: In a real app, you should request persistable permission 
                    // if you want to load the URI again after a reboot.
                    if (getContext() != null) {
                        try {
                            getContext().getContentResolver().takePersistableUriPermission(uri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } catch (SecurityException e) {
                            // This might happen if it's not a document URI
                        }
                    }
                }
            }
    );

    public static AccountFragment newInstance(String username) {
        AccountFragment fragment = new AccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvChangePic = view.findViewById(R.id.tvChangePic);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvWalletBalance = view.findViewById(R.id.tvWalletBalance);
        btnLogout = view.findViewById(R.id.btnLogout);

        tvUsername.setText(username);
        loadUserData();

        tvChangePic.setOnClickListener(v -> mGetContent.launch("image/*"));
        ivProfilePic.setOnClickListener(v -> mGetContent.launch("image/*"));

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }

    private void loadUserData() {
        if (dbHelper.isAdmin(username)) {
            tvWalletBalance.setVisibility(View.GONE);
        } else {
            double balance = dbHelper.getBalance(username);
            tvWalletBalance.setText(String.format(Locale.getDefault(), "Wallet: $%.2f", balance));
        }

        String savedImageUri = dbHelper.getImageUri(username);
        if (savedImageUri != null) {
            try {
                ivProfilePic.setImageURI(Uri.parse(savedImageUri));
            } catch (Exception e) {
                // If the URI is no longer valid or accessible
                ivProfilePic.setImageResource(android.R.drawable.sym_def_app_icon);
            }
        }
    }
}
