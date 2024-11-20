package com.example.animatesplash.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.animatesplash.Adapter.CastListAdapter;
import com.example.animatesplash.Adapter.CategoryEachFilmAdapter;
import com.example.animatesplash.Adapter.FilmListAdapter;
import com.example.animatesplash.Domains.Film;
import com.example.animatesplash.R;
import com.example.animatesplash.databinding.ActivityWatchVideoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WatchVideoActivity extends AppCompatActivity {

    ActivityWatchVideoBinding binding;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWatchVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(v -> finish());

        database = FirebaseDatabase.getInstance();

        initTopMoving();
        initUpcoming();

        setVariable();
    }

    private void setVariable() {
        Film item = (Film) getIntent().getSerializableExtra("FILM_OBJECT");

        binding.titleTxt.setText(item.getTitle());
        binding.imbdTxt.setText("IMBD " + item.getImbd());
        binding.movieTimeTxt.setText(item.getYear() + " - " + item.getTime());
        binding.movieSumery.setText(item.getDescription());


        if (item.getGenre() != null) {
            binding.genreView.setAdapter(new CategoryEachFilmAdapter(item.getGenre()));
            binding.genreView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }

        if (item.getCasts() != null) {
            binding.CastView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.CastView.setAdapter(new CastListAdapter(item.getCasts()));
        }

        binding.share.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this movie: " + getTitle());
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });



        VideoView videoView = findViewById(R.id.videoView);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        Uri videoUrl = Uri.parse(item.getTrailer());
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoUrl);
        videoView.requestFocus();

        videoView.start();
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
                        binding.recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(WatchVideoActivity.this,
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
                        binding.recyclerViewTopMovie.setLayoutManager(new LinearLayoutManager(WatchVideoActivity.this,
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

}