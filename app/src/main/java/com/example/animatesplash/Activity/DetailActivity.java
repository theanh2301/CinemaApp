package com.example.animatesplash.Activity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.animatesplash.Adapter.CastListAdapter;
import com.example.animatesplash.Adapter.CategoryEachFilmAdapter;
import com.example.animatesplash.Database.DatabaseHelper;
import com.example.animatesplash.Domains.Film;
import com.example.animatesplash.databinding.ActivityDetailBinding;

import java.io.File;

import eightbitlab.com.blurview.RenderScriptBlur;


public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setVariable();

    }

    @SuppressLint("SetTextI18n")
    private void setVariable() {
        Film item = (Film) getIntent().getSerializableExtra("Object");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new GranularRoundedCorners(0,0,50,50));

        Glide.with(this)
                .load(item.getPoster())
                .apply(requestOptions)
                .into(binding.filmPic);

        binding.titleTxt.setText(item.getTitle());
        binding.imbdTxt.setText("IMBD " + item.getImbd());
        binding.movieTimeTxt.setText(item.getYear() + " - " + item.getTime());
        binding.movieSumery.setText(item.getDescription());

        binding.backImg.setOnClickListener(v -> finish());

        float radius = 10f;
        View decorView = getWindow().getDecorView();
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();

        binding.blurView.setupWith(rootView, new RenderScriptBlur(this))
                .setFrameClearDrawable(windowBackground)
                .setBlurRadius(radius);
        binding.blurView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        binding.blurView.setClipToOutline(true);

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

        binding.watchTrailerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, WatchVideoActivity.class);
                intent.putExtra("FILM_OBJECT", item);

                startActivity(intent);
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

                DatabaseHelper databaseHelper = new DatabaseHelper(DetailActivity.this);

                boolean isInserted = databaseHelper.addFavorite(film);

                if (isInserted) {
                    Toast.makeText(DetailActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(DetailActivity.this, FavoriteActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(DetailActivity.this, "Failed to Add to Favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item != null && item.getTrailer() != null) {
                    Toast.makeText(DetailActivity.this, "Downloading " + item.getTitle(), Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(DetailActivity.this, "Unable to download video. URL is invalid.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}