package com.example.animatesplash.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animatesplash.Adapter.VideoItemAdapter;
import com.example.animatesplash.Domains.VideoItem;
import com.example.animatesplash.R;

import java.util.ArrayList;

public class WatchVideoActivity extends AppCompatActivity {
/*
    private RecyclerView recyclerView;
    private VideoItemAdapter videoItemAdapter;
    private ArrayList<VideoItem> itemList;

    @SuppressLint("MissingInflatedId")*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);

       /* recyclerView = findViewById(R.id.recyclerViewVideo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();
        itemList.add(new VideoItem("title1", "year - time", "0", "icon"));
        itemList.add(new VideoItem("title2", "year - time", "1", "logo1"));
        itemList.add(new VideoItem("title3", "year - time", "2", "logo2"));
        itemList.add(new VideoItem("title4", "year - time", "3", "img3"));
        itemList.add(new VideoItem("title5", "year - time", "4", "img2"));
        itemList.add(new VideoItem("title0", "year - time", "5", "img1"));

        videoItemAdapter = new VideoItemAdapter(itemList);
        recyclerView.setAdapter(videoItemAdapter);
*/
    }
}