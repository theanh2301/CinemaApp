package com.example.animatesplash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.animatesplash.Database.DatabaseHelper;
import com.example.animatesplash.Domains.Film;
import com.example.animatesplash.R;

import java.util.List;

public class FavoriteAdapter extends BaseAdapter {

    private final Context context;
    private final List<Film> favoriteMovies;
    private DatabaseHelper databaseHelper;

    public FavoriteAdapter(Context context, List<Film> favoriteMovies) {
        this.context = context;
        this.favoriteMovies = favoriteMovies;
    }

    @Override
    public int getCount() {
        return favoriteMovies.size();
    }

    @Override
    public Object getItem(int position) {
        return favoriteMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Film movie = favoriteMovies.get(position);

        holder.textViewTitle.setText(movie.getTitle());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(context)
                .load(movie.getPoster())
                .apply(requestOptions)
                .into(holder.imageViewPoster);

        holder.buttonDelete.setOnClickListener(v -> {
            if (databaseHelper == null) {
                databaseHelper = new DatabaseHelper(context);
            }

            boolean isDeleted = databaseHelper.deleteFavorite(movie.getTitle());
            if (isDeleted) {
                favoriteMovies.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to remove", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        private final TextView textViewTitle;
        private final ImageView imageViewPoster;
        private final ImageView buttonDelete;

        public ViewHolder(View itemView) {
            textViewTitle = itemView.findViewById(R.id.titleTxt);
            imageViewPoster = itemView.findViewById(R.id.videoPic);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
