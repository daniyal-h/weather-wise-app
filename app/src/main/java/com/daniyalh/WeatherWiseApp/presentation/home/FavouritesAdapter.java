package com.daniyalh.WeatherWiseApp.presentation.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daniyalh.WeatherWiseApp.R;

import java.util.List;
import java.util.Random;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {
    private final List<String> favouriteCities;
    private final Context context;
    private final OnItemClickListener listener;
    private final int[] icons = {
            R.drawable.ic_art1, R.drawable.ic_art2, R.drawable.ic_art3,
            R.drawable.ic_art4, R.drawable.ic_art5, R.drawable.ic_art6,
            R.drawable.ic_art7, R.drawable.ic_art8, R.drawable.ic_art9,
            R.drawable.ic_art10, R.drawable.ic_art11, R.drawable.ic_art12,
            R.drawable.ic_art13, R.drawable.ic_art14, R.drawable.ic_art15,
            R.drawable.ic_art16, R.drawable.ic_art17, R.drawable.ic_art18,
            R.drawable.ic_art19, R.drawable.ic_art20
    };


    public interface OnItemClickListener {
        void onItemClick(String city);
    }

    public FavouritesAdapter(Context context, List<String> favouriteCities, OnItemClickListener listener) {
        this.context = context;
        this.favouriteCities = favouriteCities;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favourite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String city = favouriteCities.get(position);
        holder.cityName.setText(city);

        // Randomly assign a drawableStart icon
        int randomIcon = icons[new Random().nextInt(icons.length)];
        holder.cityName.setCompoundDrawablesWithIntrinsicBounds(randomIcon, 0, 0, 0);

        holder.bind(city, listener);
    }

    @Override
    public int getItemCount() {
        return favouriteCities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityName;

        ViewHolder(View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.favourite_text_view);
        }

        void bind(final String displayName, final OnItemClickListener listener) {
            // button click for a specific favourite
            itemView.setOnClickListener(v -> listener.onItemClick(displayName));
        }
    }
}