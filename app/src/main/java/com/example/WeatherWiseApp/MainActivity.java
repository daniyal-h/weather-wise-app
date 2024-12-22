// MainActivity.java
package com.example.WeatherWiseApp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.WeatherWiseApp.logic.IWeatherCallback;
import com.example.WeatherWiseApp.logic.WeatherManager;
import com.example.WeatherWiseApp.objects.City;
import com.example.WeatherWiseApp.presentation.CityProvider;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private WeatherManager weatherManager;
    private CityProvider cityProvider;
    private EditText cityInput;
    private TextView cityTextView, weatherDetails;
    private Button fetchWeatherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

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
        cityProvider = new CityProvider(cityInput);
        weatherManager = new WeatherManager();
    }

    private void setEventListeners() {
        fetchWeatherButton.setOnClickListener(v -> {
            String cityName = cityProvider.getCity();
            if (cityName.isEmpty()) {
                showToast("Please enter a city name", Toast.LENGTH_SHORT);
                return;
            }

            City defaultCity = new City(cityName);

            // Update the TextView with city name
            cityTextView.setText("City: " + defaultCity.getCity());

            fetchWeather(defaultCity); // Fetch weather data asynchronously
        });
    }

    private void fetchWeather(City city) {
        weatherManager.getWeatherJSON(MainActivity.this, city, new IWeatherCallback() {
            @Override
            public void onSuccess(String response) {
                // Update weather details
                weatherManager.setWeather(city, response);
                String[] weather = city.getWeather();

                weatherDetails.setText(""); // Clear previous details
                for (String detail : weather) {
                    weatherDetails.append(detail + "\n");
                }
            }

            @Override
            public void onError(String error) {
                showToast("Error fetching weather: " + error, Toast.LENGTH_LONG);
                Log.e(TAG, "Error fetching weather for city " + city.getCity() + " - " + error);
            }
        });
    }

    private void showToast(String message, int duration) {
        Toast.makeText(MainActivity.this, message, duration).show();
    }
}