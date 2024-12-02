package com.example.animatesplash.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.animatesplash.Adapter.VideoAdapter;
import com.example.animatesplash.Domains.Film;
import com.example.animatesplash.R;
import com.example.animatesplash.databinding.ActivityShortVideoBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShortVideoActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    ActivityShortVideoBinding binding;
    private List<Film> videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShortVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.shortVideo);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.explorer) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (id == R.id.favorite) {
                startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }   else if (id == R.id.shortVideo) {
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


        database = FirebaseDatabase.getInstance();
        initUpcoming();
        initTopMoving();

    }

    private void initUpcoming(){
        DatabaseReference myRef = database.getReference("Upcomming");
        binding.progressBarShort.setVisibility(View.VISIBLE);
        videoList = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue:snapshot.getChildren()) {
                        videoList.add(issue.getValue(Film.class));
                    }
                    binding.viewPaper2.setAdapter(new VideoAdapter(videoList));
                    binding.progressBarShort.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initTopMoving(){
        DatabaseReference myRef = database.getReference("Items");
        binding.progressBarShort.setVisibility(View.VISIBLE);
        videoList = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue:snapshot.getChildren()) {
                        videoList.add(issue.getValue(Film.class));
                    }
                    binding.viewPaper2.setAdapter(new VideoAdapter(videoList));
                    binding.progressBarShort.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}