package com.example.animatesplash.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.animatesplash.Adapter.FavoriteAdapter;
import com.example.animatesplash.Database.DatabaseHelper;
import com.example.animatesplash.Domains.Film;
import com.example.animatesplash.R;
import com.example.animatesplash.databinding.ActivityFavoriteBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    ActivityFavoriteBinding binding;
    private List<Film> favoriteMovies;
    private FavoriteAdapter favoriteAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.favorite);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            if (id == R.id.explorer) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (id == R.id.favorite) {
                return true;
            }   else if (id == R.id.shortVideo) {
                startActivity(new Intent(getApplicationContext(), ShortVideoActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }   else if (id == R.id.profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else {
                return false;
            }
        });

        // Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Cấu hình RecyclerView
        setupRecyclerView();

        // Lấy và hiển thị danh sách phim yêu thích
        loadFavoriteMovies();
    }

    private void setupRecyclerView() {
        binding.favRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadFavoriteMovies() {
        List<Film> favoriteMovies = databaseHelper.getAllFavorites();
        if (favoriteMovies.isEmpty()) {
            Toast.makeText(this, "No favorite movies found!", Toast.LENGTH_SHORT).show();
        }

        // Gắn danh sách vào adapter
        favoriteAdapter = new FavoriteAdapter(favoriteMovies);
        binding.favRecyclerView.setAdapter(favoriteAdapter);
    }
}