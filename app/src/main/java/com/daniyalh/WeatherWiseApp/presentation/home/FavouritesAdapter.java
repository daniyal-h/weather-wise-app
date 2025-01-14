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

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {
    private final List<String> favouriteCities;
    private final Context context;
    private final OnItemClickListener listener;

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
        holder.bind(city, listener);
    }

    @Override
    public int getItemCount() {
        return favouriteCities.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
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