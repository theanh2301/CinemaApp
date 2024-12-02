package com.example.animatesplash.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animatesplash.Database.DatabaseHelper;
import com.example.animatesplash.Domains.Film;
import com.example.animatesplash.R;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private final List<Film> favoriteMovies;
    private DatabaseHelper databaseHelper;

    public FavoriteAdapter(List<Film> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Film movie = favoriteMovies.get(position);

        // Gán dữ liệu vào View
        holder.textViewTitle.setText(movie.getTitle());
        holder.textViewTime.setText(movie.getTime());

        // Hiển thị poster phim với Glide
        Glide.with(holder.itemView.getContext())
                .load(movie.getPoster())
                .into(holder.imageViewPoster);

        // Xóa phim khỏi danh sách yêu thích
        holder.buttonDelete.setOnClickListener(v -> {
            if (databaseHelper == null) {
                databaseHelper = new DatabaseHelper(holder.itemView.getContext());
            }

            boolean isDeleted = databaseHelper.deleteFavorite(movie.getTitle());
            if (isDeleted) {
                favoriteMovies.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(holder.itemView.getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(holder.itemView.getContext(), "Failed to remove", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteMovies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewTime;
        private final ImageView imageViewPoster;
        private final Button buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titleTxt);
            textViewTime = itemView.findViewById(R.id.timeTxt);
            imageViewPoster = itemView.findViewById(R.id.videoPic);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
