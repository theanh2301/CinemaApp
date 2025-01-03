package com.example.animatesplash.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.animatesplash.Adapter.FilmListAdapter;
import com.example.animatesplash.Adapter.SlidersAdapter;
import com.example.animatesplash.Domains.Film;
import com.example.animatesplash.Domains.SliderItems;
import com.example.animatesplash.R;
import com.example.animatesplash.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FilmListAdapter filmListAdapter;
    private static final int REQUEST_CODE_VOICE_INPUT = 1;
    private final ArrayList<Film> movieList = new ArrayList<>();
    ActivityMainBinding binding;
    private FirebaseDatabase database;
    private final Handler sliderHandler = new Handler();
    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            binding.viewPager2.setCurrentItem(binding.viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        initBanner();
        initTopMoving();
        initUpcoming();
        initNewMovie();
        initMovie();
        initAnime();
        initHorrorMovie();
        initActionMovie();
        initVietNamMovie();

        binding.movieRecyclerView.setVisibility(View.GONE);
        filmListAdapter = new FilmListAdapter(movieList);
        binding.movieRecyclerView.setAdapter(filmListAdapter);
        binding.micButton.setOnClickListener(v -> startVoiceInput());
        binding.searchButton.setOnClickListener(v -> {
            String query = binding.searchEditText.getText().toString().trim();
            searchMovies(query);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.explorer);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.explorer) {
                return true;
            } else if (id == R.id.favorite) {
                startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (id == R.id.shortVideo) {
                startActivity(new Intent(getApplicationContext(), ShortVideoActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (id == R.id.profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else {
                return false;
            }
        });

    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        try {
            startActivityForResult(intent, REQUEST_CODE_VOICE_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Thiết bị của bạn không hỗ trợ nhập giọng nói!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_VOICE_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                binding.searchEditText.setText(results.get(0));
            }
        }
    }

    // Hàm tìm kiếm
    private void searchMovies(String query) {
        DatabaseReference databaseReference = database.getReference("Items");

        if (query.isEmpty()) {
            movieList.clear();
            filmListAdapter.notifyDataSetChanged();
            binding.movieRecyclerView.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.orderByChild("Title")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        movieList.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Film film = snapshot.getValue(Film.class);
                            if (film != null) {
                                movieList.add(film);
                            }
                        }

                        if (movieList.isEmpty()) {
                            binding.movieRecyclerView.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Không tìm thấy phim nào khớp với từ khóa.", Toast.LENGTH_SHORT).show();
                        } else {
                            binding.movieRecyclerView.setVisibility(View.VISIBLE);
                        }
                        filmListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "Lỗi khi lấy dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void initUpcoming(){
        DatabaseReference myRef = database.getReference("Upcomming");
        binding.progressBarUpcoming.setVisibility(View.VISIBLE);
        ArrayList<Film> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue:snapshot.getChildren()) {
                        items.add(issue.getValue(Film.class));
                    }
                    if (!items.isEmpty()){
                        binding.recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerViewUpcoming.setAdapter(new FilmListAdapter(items));
                    }

                    binding.progressBarUpcoming.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initTopMoving(){
        DatabaseReference myRef = database.getReference("Items");
        binding.progressBar3.setVisibility(View.VISIBLE);
        ArrayList<Film> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue:snapshot.getChildren()) {
                        items.add(issue.getValue(Film.class));
                    }
                    if (!items.isEmpty()){
                        binding.recyclerViewTopMovie.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerViewTopMovie.setAdapter(new FilmListAdapter(items));
                    }

                    binding.progressBar3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initNewMovie(){
        DatabaseReference myRef = database.getReference("NewMovie");
        binding.progressBarNewMovie.setVisibility(View.VISIBLE);
        ArrayList<Film> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue:snapshot.getChildren()) {
                        items.add(issue.getValue(Film.class));
                    }
                    if (!items.isEmpty()){
                        binding.recyclerViewNewMovie.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerViewNewMovie.setAdapter(new FilmListAdapter(items));
                    }

                    binding.progressBarNewMovie.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initMovie() {
        DatabaseReference myRef = database.getReference("Movie");
        binding.progressBarMovie.setVisibility(View.VISIBLE);
        ArrayList<Film> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue:snapshot.getChildren()) {
                        items.add(issue.getValue(Film.class));
                    }
                    if (!items.isEmpty()){
                        binding.gridViewMovie.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.gridViewMovie.setAdapter(new FilmListAdapter(items));
                    }

                    binding.progressBarMovie.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initAnime(){
        DatabaseReference myRef = database.getReference("Anime");
        binding.progressBarAnime.setVisibility(View.VISIBLE);
        ArrayList<Film> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue:snapshot.getChildren()) {
                        items.add(issue.getValue(Film.class));
                    }
                    if (!items.isEmpty()){
                        binding.recyclerViewAnime.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerViewAnime.setAdapter(new FilmListAdapter(items));
                    }

                    binding.progressBarAnime.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initHorrorMovie(){
        DatabaseReference myRef = database.getReference("Horror");
        binding.progressBarHorrorMovie.setVisibility(View.VISIBLE);
        ArrayList<Film> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue:snapshot.getChildren()) {
                        items.add(issue.getValue(Film.class));
                    }
                    if (!items.isEmpty()){
                        binding.recyclerViewHorrorMovie.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerViewHorrorMovie.setAdapter(new FilmListAdapter(items));
                    }

                    binding.progressBarHorrorMovie.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initActionMovie(){
        DatabaseReference myRef = database.getReference("Action");
        binding.progressBarActionMovies.setVisibility(View.VISIBLE);
        ArrayList<Film> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue:snapshot.getChildren()) {
                        items.add(issue.getValue(Film.class));
                    }
                    if (!items.isEmpty()){
                        binding.recyclerViewActionMovies.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerViewActionMovies.setAdapter(new FilmListAdapter(items));
                    }

                    binding.progressBarActionMovies.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initVietNamMovie(){
        DatabaseReference myRef = database.getReference("Vietnamese");
        binding.progressBarVietnameseMovies.setVisibility(View.VISIBLE);
        ArrayList<Film> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue:snapshot.getChildren()) {
                        items.add(issue.getValue(Film.class));
                    }
                    if (!items.isEmpty()){
                        binding.recyclerViewVietnameseMovies.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerViewVietnameseMovies.setAdapter(new FilmListAdapter(items));
                    }

                    binding.progressBarVietnameseMovies.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initBanner() {
        DatabaseReference myRef = database.getReference("Banners");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue:snapshot.getChildren()) {
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void banners(ArrayList<SliderItems> items){
        binding.viewPager2.setAdapter(new SlidersAdapter(items, binding.viewPager2));
        binding.viewPager2.setClipToPadding(false);
        binding.viewPager2.setClipChildren(false);
        binding.viewPager2.setOffscreenPageLimit(3);
        binding.viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        binding.viewPager2.setPageTransformer(compositePageTransformer);
        binding.viewPager2.setCurrentItem(1);
        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume(){
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2000);
    }
}