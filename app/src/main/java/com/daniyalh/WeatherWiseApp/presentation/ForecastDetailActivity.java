package com.daniyalh.WeatherWiseApp.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daniyalh.WeatherWiseApp.R;

public class ForecastDetailActivity extends AppCompatActivity {
    private WeatherController weatherController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_forecast_detail);
        setClasses();
        setButtonListeners();
    }

    private void setClasses() {
        weatherController = WeatherController.getInstance();
    }

    private void setButtonListeners() {
        // goBackButton.setOnClickListener(v -> finish()); // immediate weather
    }
}
