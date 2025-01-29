package com.daniyalh.WeatherWiseApp.presentation.forecast;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.logic.forecast.ForecastManager;
import com.daniyalh.WeatherWiseApp.objects.City;

public class ForecastPage extends AppCompatActivity {
    private ForecastController forecastController;
    private ForecastUIManager forecastUIManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_detail);

        setClasses();

        showLoadingIcon(true);
        forecastController.fetchForecast();
    }

    private void setClasses() {
        ForecastManager forecastManager = new ForecastManager(this);
        forecastController = new ForecastController(this, forecastManager);
        forecastUIManager = new ForecastUIManager(this);
    }

    public void showLoadingIcon(boolean visible) {
        forecastUIManager.showLoadingIcon(visible);
    }

    public void showToast(String message, int duration) {
        forecastUIManager.showToast(message, duration);
    }

    public void displayForecasts(City city) {
        forecastUIManager.displayForecasts(city);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        forecastUIManager.resetUI();
    }
}