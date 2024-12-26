package com.daniyalh.WeatherWiseApp.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.logic.CityManager;
import com.daniyalh.WeatherWiseApp.logic.WeatherManager;

public class MainActivity extends AppCompatActivity {
    private WeatherManager weatherManager;
    private CityManager cityManager;

    private UIManager uiManager;
    private WeatherController weatherController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        initializeLogicClasses();
        initializeUIManager();
        initializeWeatherController();
        setEventListeners();
    }

    private void initializeLogicClasses() {
        cityManager = new CityManager();
        weatherManager = new WeatherManager(this);
    }

    private void initializeUIManager() {
        View rootView = findViewById(R.id.root_layout);
        uiManager = new UIManager(this, rootView);
    }

    private void initializeWeatherController() {
        weatherController = new WeatherController(weatherManager, cityManager, uiManager);
    }

    private void setEventListeners() {
        uiManager.getGetWeatherButton().setOnClickListener(v -> {
            uiManager.hideKeyboard(v);

            // Get City Name Input
            String cityName = uiManager.getCityName();
            if (cityName.isEmpty()) {
                uiManager.showToast("Please enter a city name", Toast.LENGTH_SHORT);
                return;
            }

            // Fetch Weather Data
            weatherController.fetchWeather(cityName);
        });
    }
}