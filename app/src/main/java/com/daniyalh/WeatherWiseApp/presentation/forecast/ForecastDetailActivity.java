package com.daniyalh.WeatherWiseApp.presentation.forecast;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.logic.forecast.ForecastManager;


public class ForecastDetailActivity extends AppCompatActivity {
    private ForecastManager forecastManager;
    private ForecastController forecastController;

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
        loadingIconLottie = findViewById(R.id.loading_forecast_icon_lottie);
        goBackButton = findViewById(R.id.back_to_weather_button);
    }


    private void setClasses() {
        forecastManager = new ForecastManager(this);
        forecastController = new ForecastController(this, forecastManager);
    }

    private void setButtonListeners() {
        goBackButton.setOnClickListener(v -> finish()); //
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
}
