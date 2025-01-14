package com.daniyalh.WeatherWiseApp.presentation.forecast;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.logic.forecast.ForecastManager;
import com.daniyalh.WeatherWiseApp.objects.City;
import com.daniyalh.WeatherWiseApp.objects.Forecast;
import com.daniyalh.WeatherWiseApp.presentation.home.FavouritesAdapter;


public class ForecastDetailActivity extends AppCompatActivity {
    private ForecastManager forecastManager;
    private ForecastController forecastController;

    private TextView displayNameTextView;
    private RecyclerView forecastsRecyclerView;
    private Button goBackButton;
    private LottieAnimationView loadingIconLottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_detail);

        initializeUI();
        setClasses();
        setButtonListeners();

        showLoadingIcon(true);
        forecastController.fetchForecast();
    }

    private void initializeUI() {
        displayNameTextView = findViewById(R.id.forecast_display_name_text_view);
        forecastsRecyclerView = findViewById(R.id.forecasts_recycler_view);
        forecastsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadingIconLottie = findViewById(R.id.loading_forecast_icon_lottie);
        goBackButton = findViewById(R.id.back_to_weather_button);
    }

    private void setClasses() {
        forecastManager = new ForecastManager(this);
        forecastController = new ForecastController(this, forecastManager);
    }

    private void setButtonListeners() {
        goBackButton.setOnClickListener(v -> finish());
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

    public void setDisplayName(String displayName) {
        displayNameTextView.setText(displayName);
    }

    public void displayForecasts(Forecast[] forecasts) {
        forecastsRecyclerView.setAdapter(new ForecastsAdapter(this, forecasts));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resetUI();
    }

    public void resetUI() {
        displayNameTextView.setText("");
    }
}
