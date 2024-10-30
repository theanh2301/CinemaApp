package com.example.animatesplash.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animatesplash.Activity.WatchVideoActivity;
import com.example.animatesplash.Domains.VideoItem;
import com.example.animatesplash.R;

import java.util.ArrayList;

public class VideoItemAdapter extends RecyclerView.Adapter<VideoItemAdapter.ViewHolder> {
    ArrayList<VideoItem> items;
    private Context context;

    public VideoItemAdapter(ArrayList<VideoItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTxt.setText(items.get(position).getTitleTxt());
        holder.timeTxt.setText(items.get(position).getTimeTxt());
        holder.imbd.setText("IMBD " + items.get(position).getImbd());

        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(items.get(position).getVideoPic(),
                "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.videoPic);

        holder.videoPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), WatchVideoActivity.class);
                intent.putExtra("object", items.get(holder.getAdapterPosition()));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, timeTxt, imbd;
        ImageView videoPic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            imbd = itemView.findViewById(R.id.imbdTxt);
            videoPic = itemView.findViewById(R.id.videoPic);
        }
    }
}
