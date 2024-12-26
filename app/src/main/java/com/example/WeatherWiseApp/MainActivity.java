// MainActivity.java
package com.example.WeatherWiseApp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.WeatherWiseApp.logic.WeatherCallback;
import com.example.WeatherWiseApp.logic.WeatherManager;
import com.example.WeatherWiseApp.objects.City;
import com.example.WeatherWiseApp.presentation.CityProvider;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private WeatherManager weatherManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        // Reference UI components
        EditText cityInput = findViewById(R.id.defaultCityInputTextView);
        TextView cityTextView = findViewById(R.id.cityTextView);
        Button fetchWeatherButton = findViewById(R.id.buttonFetchWeather);
        TextView weatherDetails = findViewById(R.id.weatherDetailsTextView);

        // Initialize your logic classes
        CityProvider cityProvider = new CityProvider(cityInput);
        weatherManager = new WeatherManager();

        // Set button click listener
        fetchWeatherButton.setOnClickListener(v -> {
            String cityName = cityProvider.getCity();
            if (cityName.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                return;
            }

            City defaultCity = new City(cityName);

            // Update the TextView with city name
            cityTextView.setText("City: " + defaultCity.getCity());

            // Fetch weather data asynchronously
            weatherManager.getWeatherJSON(MainActivity.this, defaultCity, new WeatherCallback() {
                @Override
                public void onSuccess(String response) {
                    // Update weather details
                    weatherManager.setWeather(defaultCity, response);
                    String[] weather = defaultCity.getWeather();

                    weatherDetails.setText(""); // Clear previous details
                    for (String detail : weather) {
                        weatherDetails.append(detail + "\n");
                    }
                }

                @Override
                public void onError(String error) {
                    // Handle the error (display a message to the user)
                    Toast.makeText(MainActivity.this, "Error fetching weather: " + error, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error fetching weather: " + error);
                }
            });
        });
    }
}