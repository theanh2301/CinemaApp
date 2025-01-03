package com.example.animatesplash.Activity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.animatesplash.Adapter.CastListAdapter;
import com.example.animatesplash.Adapter.CategoryEachFilmAdapter;
import com.example.animatesplash.Adapter.FilmListAdapter;
import com.example.animatesplash.Database.DatabaseHelper;
import com.example.animatesplash.Domains.Film;
import com.example.animatesplash.R;
import com.example.animatesplash.databinding.ActivityWatchVideoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WatchVideoActivity extends AppCompatActivity {

    ActivityWatchVideoBinding binding;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWatchVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        initTopMoving();
        initUpcoming();
        initNewMovie();
        initAnime();
        initHorrorMovie();
        initActionMovie();
        initVietNamMovie();

        setVariable();

        binding.back.setOnClickListener(v -> finish());
    }

    @SuppressLint("ClickableViewAccessibility")
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

        binding.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (item != null && item.getTrailer() != null) {

                    Toast.makeText(WatchVideoActivity.this, "Downloading " + item.getTitle(), Toast.LENGTH_SHORT).show();

                    String videoUrl = item.getTrailer();
                    String fileName = item.getTitle() + ".mp4";
                    File downloadDirectory = new File(getExternalFilesDir(null), "Downloads");

                    if (!downloadDirectory.exists()) {
                        downloadDirectory.mkdir();
                    }

                    File destinationFile = new File(downloadDirectory, fileName);

                    DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(videoUrl);
                    DownloadManager.Request request = new DownloadManager.Request(uri)
                            .setTitle(item.getTitle())
                            .setDescription("Downloading video...")
                            .setDestinationUri(Uri.fromFile(destinationFile))
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                    if (downloadManager != null) {
                        downloadManager.enqueue(request);
                    }

                } else {
                    Toast.makeText(WatchVideoActivity.this, "Unable to download video. URL is invalid.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Film film = new Film(
                        item.getTitle(),
                        item.getDescription(),
                        item.getPoster(),
                        item.getTime(),
                        item.getTrailer(),
                        item.getImbd(),
                        item.getYear(),
                        item.getGenre()
                );

                DatabaseHelper databaseHelper = new DatabaseHelper(WatchVideoActivity.this);

                boolean isInserted = databaseHelper.addFavorite(film);

                if (isInserted) {
                    Toast.makeText(WatchVideoActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(WatchVideoActivity.this, FavoriteActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(WatchVideoActivity.this, "Failed to Add to Favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.share.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this movie: " + getTitle());
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        VideoView videoView = findViewById(R.id.videoView);
        Handler handler = new Handler();

        Runnable hideControlButtons = () -> {
            binding.pauseBtn.setVisibility(View.GONE);
            binding.forward.setVisibility(View.GONE);
            binding.replay.setVisibility(View.GONE);
            binding.btnFullscreen.setVisibility(View.GONE);
        };

        Uri videoUrl = Uri.parse(item.getTrailer());
        videoView.setVideoURI(videoUrl);
        videoView.requestFocus();

        videoView.setOnPreparedListener(mp -> {
            binding.progressBarVideo.setVisibility(View.GONE);
            videoView.start();
            binding.playBtn.setVisibility(View.GONE);
            binding.pauseBtn.setVisibility(View.VISIBLE);
            binding.forward.setVisibility(View.VISIBLE);
            binding.replay.setVisibility(View.VISIBLE);
            binding.btnFullscreen.setVisibility(View.VISIBLE);
            handler.postDelayed(hideControlButtons, 3000);
        });

        binding.playBtn.setOnClickListener(v -> {
            videoView.start();
            binding.playBtn.setVisibility(View.GONE);
            binding.pauseBtn.setVisibility(View.VISIBLE);
            binding.forward.setVisibility(View.VISIBLE);
            binding.replay.setVisibility(View.VISIBLE);
            binding.btnFullscreen.setVisibility(View.VISIBLE);
            handler.postDelayed(hideControlButtons, 3000);
        });

        binding.pauseBtn.setOnClickListener(v -> {
            videoView.pause();
            binding.playBtn.setVisibility(View.VISIBLE);
            binding.pauseBtn.setVisibility(View.GONE);
            binding.forward.setVisibility(View.VISIBLE);
            binding.replay.setVisibility(View.VISIBLE);
            binding.btnFullscreen.setVisibility(View.VISIBLE);
            handler.removeCallbacks(hideControlButtons);
        });

        videoView.setOnCompletionListener(mp -> {
            binding.playBtn.setVisibility(View.VISIBLE);
            binding.pauseBtn.setVisibility(View.GONE);
            binding.forward.setVisibility(View.GONE);
            binding.replay.setVisibility(View.GONE);
            handler.removeCallbacks(hideControlButtons);
        });

        videoView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                binding.pauseBtn.setVisibility(View.VISIBLE);
                binding.forward.setVisibility(View.VISIBLE);
                binding.replay.setVisibility(View.VISIBLE);
                binding.btnFullscreen.setVisibility(View.VISIBLE);
                handler.removeCallbacks(hideControlButtons);
                handler.postDelayed(hideControlButtons, 3000);
            }
            return false;
        });

        binding.forward.setOnClickListener(v -> {
            int currentPosition = videoView.getCurrentPosition();
            int duration = videoView.getDuration();
            int newPosition = Math.min(currentPosition + 10000, duration);
            videoView.seekTo(newPosition);
            handler.removeCallbacks(hideControlButtons);
            handler.postDelayed(hideControlButtons, 3000);

        });

        binding.replay.setOnClickListener(v -> {
            int currentPosition = videoView.getCurrentPosition();
            int newPosition = Math.max(currentPosition - 10000, 0);
            videoView.seekTo(newPosition);
            handler.removeCallbacks(hideControlButtons);
            handler.postDelayed(hideControlButtons, 3000);

        });

        binding.btnFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentOrientation = getResources().getConfiguration().orientation;

                if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
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
                        binding.recyclerViewNewMovie.setLayoutManager(new LinearLayoutManager(WatchVideoActivity.this,
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
                        binding.recyclerViewAnime.setLayoutManager(new LinearLayoutManager(WatchVideoActivity.this,
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
                        binding.recyclerViewHorrorMovie.setLayoutManager(new LinearLayoutManager(WatchVideoActivity.this,
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
                        binding.recyclerViewActionMovies.setLayoutManager(new LinearLayoutManager(WatchVideoActivity.this,
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
                        binding.recyclerViewVietnameseMovies.setLayoutManager(new LinearLayoutManager(WatchVideoActivity.this,
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
}