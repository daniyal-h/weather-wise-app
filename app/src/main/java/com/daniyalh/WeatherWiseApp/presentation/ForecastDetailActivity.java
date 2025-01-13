package com.daniyalh.WeatherWiseApp.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daniyalh.WeatherWiseApp.R;


public class ForecastDetailActivity extends AppCompatActivity {
    private WeatherController weatherController;

    private Button goBackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_detail);
        initializeUI();
        setClasses();
        setButtonListeners();
    }

    private void initializeUI() {
        goBackButton = findViewById(R.id.back_to_weather_button);
    }


    private void setClasses() {
        weatherController = WeatherController.getInstance();
    }

    private void setButtonListeners() {
        goBackButton.setOnClickListener(v -> finish()); //
    }
}
