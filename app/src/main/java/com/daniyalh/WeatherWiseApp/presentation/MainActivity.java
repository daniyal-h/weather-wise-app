package com.daniyalh.WeatherWiseApp.presentation;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.logic.CityManager;
import com.daniyalh.WeatherWiseApp.logic.IWeatherCallback;
import com.daniyalh.WeatherWiseApp.logic.WeatherManager;
import com.daniyalh.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;
import com.daniyalh.WeatherWiseApp.objects.City;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private WeatherManager weatherManager;
    private CityManager cityManager; // track all cities searched
    private EditText cityInput;
    private TextView cityTextView, weatherDetails;
    private Button fetchWeatherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout2);

        initializeUI();
        initializeLogicClasses();
        setEventListeners();
    }

    private void initializeUI() {
        cityInput = findViewById(R.id.defaultCityInputTextView);
        cityTextView = findViewById(R.id.cityTextView);
        fetchWeatherButton = findViewById(R.id.buttonFetchWeather);
        weatherDetails = findViewById(R.id.weatherDetailsTextView);
    }

    private void initializeLogicClasses() {
        cityManager = new CityManager();
        weatherManager = new WeatherManager(this);
    }

    private void setEventListeners() {
        fetchWeatherButton.setOnClickListener(v -> {
            String cityName = cityInput.getText().toString().trim().toLowerCase();
            if (cityName.isEmpty()) {
                showToast("Please enter a city name", Toast.LENGTH_SHORT);
                return;
            }

            fetchWeather(cityName); // Fetch weather data asynchronously
        });
    }

    private void fetchWeather(String cityName) {
        City city = new City(cityName);
        weatherManager.getWeatherJSON(city, new IWeatherCallback() {
            @Override
            public void onSuccess(String response) {
                // Update weather details
                try {
                    cityTextView.setText(city.getCity().toUpperCase());

                    weatherManager.setWeather(city, response);
                    String[] weather = city.getWeather();

                    weatherDetails.setText(""); // Clear previous details
                    for (String detail : weather) {
                        weatherDetails.append(detail + "\n");
                    }
                    cityManager.addCity(city); // add or update record
                }
                catch (InvalidJsonParsingException e) {
                    showToast(e.getMessage(), Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onError(String error) {
                showCriticalError("Unable to fetch weather data. Please check the city name and try again.");
                Log.e(TAG, "Error fetching weather for city " + city.getCity() + " - " + error);
                resetTextBoxes();
            }
        });
    }

    private void showToast(String message, int duration) {
        Toast.makeText(MainActivity.this, message, duration).show();
    }

    private void showCriticalError(String error) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Error")
                .setMessage(error)
                .setPositiveButton("OK", null)
                .show();
    }

    private void resetTextBoxes() {
        cityTextView.setText("");
        weatherDetails.setText("");
    }

    public void setWeatherManager(WeatherManager weatherManager) {
        this.weatherManager = weatherManager;
    }
}