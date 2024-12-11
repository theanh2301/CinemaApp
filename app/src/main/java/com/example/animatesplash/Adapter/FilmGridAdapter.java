package com.example.animatesplash.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.animatesplash.Activity.DetailActivity;
import com.example.animatesplash.Database.DatabaseHelper;
import com.example.animatesplash.Domains.Film;
import com.example.animatesplash.R;

import java.util.List;

public class FilmGridAdapter extends BaseAdapter {
    private final List<Film> items;
    private final Context context;
    private DatabaseHelper databaseHelper;

    public FilmGridAdapter(Context context, List<Film> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.film_viewholder, parent, false);
            holder = new ViewHolder();
            holder.titleTxt = convertView.findViewById(R.id.nameTxt);
            holder.pic = convertView.findViewById(R.id.pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Film film = items.get(position);
        holder.titleTxt.setText(film.getTitle());

        RequestOptions requestOptions = new RequestOptions()
                .transform(new CenterCrop(), new RoundedCorners(30));

        Glide.with(context)
                .load(film.getPoster())
                .apply(requestOptions)
                .into(holder.pic);

        convertView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("Object", film);
            context.startActivity(intent);
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView titleTxt;
        ImageView pic;
    }
}
