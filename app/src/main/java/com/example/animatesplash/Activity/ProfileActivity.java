package com.example.animatesplash.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.animatesplash.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
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
                startActivity(new Intent(getApplicationContext(), ShortVideoActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }   else if (id == R.id.profile) {
                return true;
            } else {
                return false;
            }
        });
    }
}