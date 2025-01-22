package com.daniyalh.WeatherWiseApp.presentation.forecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.objects.Forecast;

public class ForecastsAdapter extends RecyclerView.Adapter<ForecastsAdapter.forecastViewHolder> {
    private final Context context;
    private final Forecast[] forecasts;

    public ForecastsAdapter(Context context, Forecast[] forecasts) {
        this.context = context;
        this.forecasts = forecasts;
    }
    @NonNull
    @Override
    public forecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_forecast, parent, false);
        return new forecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull forecastViewHolder holder, int position) {
        Forecast forecast = forecasts[position];

        String dateAndTime = forecast.getDateTime();
        String description = forecast.getDescription();
        String temp = (int) forecast.getTemp() + "°C";
        String feelsLike = "Feels Like " + (int) forecast.getFeelsLike();
        String wind = "Wind: " + (int) forecast.getWindSpeed() + " km/h";

        holder.dateAndTimeTextView.setText(dateAndTime);
        holder.descriptionTextView.setText(description);
        holder.tempTextView.setText(temp);
        holder.feelsLikesTextView.setText(feelsLike);
        holder.windSpeedTextView.setText(wind);

        setForecastIcon(holder, forecast.getDescription(), forecast.isDay());
    }

    private void setForecastIcon(@NonNull forecastViewHolder holder, String iconGroup, boolean isDay) {
        int iconResource;

        // get the icon resource based on the icon group and if it's day or night
        switch(iconGroup) {
            case ("Clear"):
                iconResource = isDay ? R.drawable.icon_clear_day : R.drawable.icon_clear_night;
                break;

            case("Clouds"):
                iconResource = isDay ? R.drawable.icon_cloudy_day : R.drawable.icon_cloudy_night;
                break;

            case("Drizzle"):
                iconResource = isDay ? R.drawable.icon_drizzle_day : R.drawable.icon_drizzle_night;
                break;

            case("Rain"):
                iconResource = R.drawable.icon_rain;
                break;

            case("Snow"):
                iconResource = R.drawable.icon_snow;
                break;

            case("Thunderstorm"):
                iconResource = isDay ? R.drawable.icon_thunderstorm_day : R.drawable.icon_thunderstorm_night;
                break;

            default:
                iconResource = R.drawable.icon_atmosphere;
                break;
        }

        // set the image resources
        holder.forecastIconImageView.setImageResource(iconResource);
    }

    @Override
    public int getItemCount() {
        return forecasts.length;
    }

    static class forecastViewHolder extends RecyclerView.ViewHolder {
        TextView dateAndTimeTextView, descriptionTextView, tempTextView, feelsLikesTextView, windSpeedTextView;
        ImageView forecastIconImageView;

        forecastViewHolder(View itemView) {
            super(itemView);
            dateAndTimeTextView = itemView.findViewById(R.id.forecast_date_text_view);
            descriptionTextView = itemView.findViewById(R.id.forecast_description_text_view);
            tempTextView = itemView.findViewById(R.id.forecast_temp_text_view);
            feelsLikesTextView = itemView.findViewById(R.id.forecast_feels_like_text_view);
            windSpeedTextView = itemView.findViewById(R.id.forecast_wind_speed_text_view);
            forecastIconImageView = itemView.findViewById(R.id.forecast_icon_image_view);
        }
    }
}