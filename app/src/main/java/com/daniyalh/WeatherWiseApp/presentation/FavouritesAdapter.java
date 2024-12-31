package com.daniyalh.WeatherWiseApp.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {
    private List<String> favouriteCities;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String city);
    }

    public FavouritesAdapter(Context context, List<String> favouriteCities, OnItemClickListener listener) {
        this.context = context;
        this.favouriteCities = favouriteCities;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityName;

        ViewHolder(View itemView) {
            super(itemView);
            cityName = itemView.findViewById(android.R.id.text1);
        }

        void bind(final String city, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(city));
        }
    }
}