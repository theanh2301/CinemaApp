package com.example.animatesplash.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animatesplash.Activity.WatchVideoActivity;
import com.example.animatesplash.Domains.Film;
import com.example.animatesplash.R;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    List<Film> videoList;
    Context context;

    public VideoAdapter(List<Film> videoList){
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_video, parent, false);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.setVideoData(videoList.get(position));
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder{

        VideoView videoView;
        TextView title, movieTimeTxt, imbd;
        Button watchBtn;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.viewShortVideo);
            title = itemView.findViewById(R.id.videoTitle);
            movieTimeTxt = itemView.findViewById(R.id.movieTimeTxt);
            imbd = itemView.findViewById(R.id.imbdTxt);
            watchBtn = itemView.findViewById(R.id.watchTrailerBtn);
        }

        public void setVideoData(Film video){
            title.setText(video.getTitle());
            movieTimeTxt.setText(video.getYear() + " - " + video.getTime());
            imbd.setText("IMBD " + video.getImbd());
            videoView.setVideoPath(video.getTrailer());

            watchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WatchVideoActivity.class);
                    intent.putExtra("FILM_OBJECT", video);
                    context.startActivity(intent);
                }
            });

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();

                    float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                    float screenRatio = videoView.getWidth() / (float) videoView.getHeight();

                    float scale = videoRatio / screenRatio;
                    if (scale >= 1f) {
                        videoView.setScaleX(scale);
                    } else {
                        videoView.setScaleY(1f/scale);
                    }
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });
        }
    }
}
