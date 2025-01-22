package com.daniyalh.WeatherWiseApp.presentation.forecast;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.objects.City;

public class ForecastUIManager {
    private final ForecastPage forecastPage;
    private TextView displayNameTextView;
    private RecyclerView forecastsRecyclerView;
    private Button goBackButton;
    private LottieAnimationView loadingIconLottie;
    public ForecastUIManager(ForecastPage forecastPage) {
        this.forecastPage = forecastPage;
        initializeUI();
        setButtonListeners();
    }

    private void initializeUI() {
        displayNameTextView = forecastPage.findViewById(R.id.forecast_display_name_text_view);
        forecastsRecyclerView = forecastPage.findViewById(R.id.forecasts_recycler_view);
        forecastsRecyclerView.setLayoutManager(new LinearLayoutManager(forecastPage));
        loadingIconLottie = forecastPage.findViewById(R.id.loading_forecast_icon_lottie);
        goBackButton = forecastPage.findViewById(R.id.back_to_weather_button);
    }

    private void setButtonListeners() {
        goBackButton.setOnClickListener(v -> forecastPage.finish());
    }

    public void showLoadingIcon(boolean visible) {
        // toggle the loading icon
        if (visible) {
            loadingIconLottie.setVisibility(View.VISIBLE);
            loadingIconLottie.playAnimation();
        }
        else {
            loadingIconLottie.setVisibility(View.GONE);
            loadingIconLottie.cancelAnimation();
        }
    }

    public void displayForecasts(City city) {
        // set the display name and adapter
        String displayName = city.getCityName() + ", " + city.getCountryCode();
        setDisplayName(displayName);
        forecastsRecyclerView.setAdapter(new ForecastsAdapter(forecastPage, city.getForecasts()));
    }

    private void setDisplayName(String displayName) {
        displayNameTextView.setText(displayName);
    }

    public void resetUI() {
        displayNameTextView.setText("");
    }
}
