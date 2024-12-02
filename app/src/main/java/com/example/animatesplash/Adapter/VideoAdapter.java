package com.example.animatesplash.Adapter;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animatesplash.Activity.DetailActivity;
import com.example.animatesplash.Activity.FavoriteActivity;
import com.example.animatesplash.Activity.ShortVideoActivity;
import com.example.animatesplash.Activity.WatchVideoActivity;
import com.example.animatesplash.Database.DatabaseHelper;
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
        ImageView share, fav;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.viewShortVideo);
            title = itemView.findViewById(R.id.videoTitle);
            movieTimeTxt = itemView.findViewById(R.id.movieTimeTxt);
            imbd = itemView.findViewById(R.id.imbdTxt);
            watchBtn = itemView.findViewById(R.id.watchTrailerBtn);
            share = itemView.findViewById(R.id.share);
            fav = itemView.findViewById(R.id.fav);
        }

        public void setVideoData(Film video){
            title.setText(video.getTitle());
            movieTimeTxt.setText(video.getYear() + " - " + video.getTime());
            imbd.setText("IMBD " + video.getImbd());
            videoView.setVideoPath(video.getTrailer());

            share.setOnClickListener(v -> {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this movie: " + video.getTitle());
                context.startActivity(Intent.createChooser(shareIntent, "Share via"));
            });

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isLiked = false;

                    isLiked = !isLiked;

                    if (isLiked) {
                        fav.setImageResource(R.drawable.btn_21);
                    } else {
                        fav.setImageResource(R.drawable.btn_2);
                    }

                    ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                            fav,
                            PropertyValuesHolder.ofFloat("scaleX", 0.8f),
                            PropertyValuesHolder.ofFloat("scaleY", 0.8f));
                    scaleDown.setDuration(150);

                    scaleDown.setRepeatCount(1);
                    scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

                    scaleDown.start();
                }
            });

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
