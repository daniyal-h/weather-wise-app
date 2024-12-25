package com.daniyalh.WeatherWiseApp.presentation;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
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
    //private EditText cityInput;
    //private TextView cityTextView, weatherDetails;
    //private Button fetchWeatherButton;

    private EditText cityInputEditText;
    private TextView cityTextView, descriptionTextView,
    sunriseLabelTextView, sunsetLabelTextView, windLabelTextView, humidityLabelTextView,
    tempTextView, feelsLikeTextView, sunriseTextView, sunsetTextView, windTextView, humidityTextView;

    private LottieAnimationView sunIconLottie, moonIconLottie, windIconLottie, humidityIconLottie;

    private Button getWeatherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        initializeUI();
        initializeLogicClasses();
        setEventListeners();
    }

    private void initializeUI() {
        //cityInput = findViewById(R.id.city_input_edit_text);
        //cityTextView = findViewById(R.id.city_text_view);
        //fetchWeatherButton = findViewById(R.id.get_weather_button);
        //weatherDetails = findViewById(R.id.weatherDetailsTextView);
        cityInputEditText = findViewById(R.id.city_input_edit_text);
        cityTextView = findViewById(R.id.city_text_view);
        descriptionTextView = findViewById(R.id.description_text_view);

        sunriseLabelTextView = findViewById(R.id.sunrise_label_text_view);
        sunsetLabelTextView = findViewById(R.id.sunset_label_text_view);
        windLabelTextView = findViewById(R.id.wind_label_text_view);
        humidityLabelTextView = findViewById(R.id.humidity_label_text_view);

        getWeatherButton = findViewById(R.id.get_weather_button);

        tempTextView = findViewById(R.id.temperature_text_view);
        feelsLikeTextView = findViewById(R.id.feels_like_text_view);
        sunriseTextView = findViewById(R.id.sunrise_text_view);
        sunsetTextView = findViewById(R.id.sunset_text_view);
        windTextView = findViewById(R.id.wind_text_view);
        humidityTextView = findViewById(R.id.humidity_text_view);

        sunIconLottie = findViewById(R.id.sun_icon_lottie);
        moonIconLottie = findViewById(R.id.moon_icon_lottie);
        windIconLottie = findViewById(R.id.wind_icon_lottie);
        humidityIconLottie = findViewById(R.id.humidity_icon_lottie);

    }

    private void initializeLogicClasses() {
        cityManager = new CityManager();
        weatherManager = new WeatherManager(this);
    }

    private void setEventListeners() {
        getWeatherButton.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(cityInputEditText.getWindowToken(), 0);
            }
            cityInputEditText.clearFocus();

            String cityName = cityInputEditText.getText().toString().trim().toLowerCase();
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

                    setStaticUI();
                    showDetails(city.getWeather());

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

    private void setStaticUI() {
        sunriseLabelTextView.setVisibility(View.VISIBLE);
        sunsetLabelTextView.setVisibility(View.VISIBLE);
        windLabelTextView.setVisibility(View.VISIBLE);
        humidityLabelTextView.setVisibility(View.VISIBLE);

        sunIconLottie.setVisibility(View.VISIBLE);
        moonIconLottie.setVisibility(View.VISIBLE);
        windIconLottie.setVisibility(View.VISIBLE);
        humidityIconLottie.setVisibility(View.VISIBLE);
    }

    private void showDetails(String[] details) {
        tempTextView.setText(details[0]);
        feelsLikeTextView.setText(details[1]);
        descriptionTextView.setText(details[2]);
        humidityTextView.setText(details[3]);
        windTextView.setText(details[4]);
        sunriseTextView.setText(details[5]);
        sunsetTextView.setText(details[6]);
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
        sunriseLabelTextView.setVisibility(View.INVISIBLE);
        sunsetLabelTextView.setVisibility(View.INVISIBLE);
        windLabelTextView.setVisibility(View.INVISIBLE);
        humidityLabelTextView.setVisibility(View.INVISIBLE);
        sunIconLottie.setVisibility(View.INVISIBLE);
        moonIconLottie.setVisibility(View.INVISIBLE);
        windIconLottie.setVisibility(View.INVISIBLE);
        humidityIconLottie.setVisibility(View.INVISIBLE);
        cityTextView.setText("");
        tempTextView.setText("");
        feelsLikeTextView.setText("");
        descriptionTextView.setText("");
        sunriseTextView.setText("");
        sunsetTextView.setText("");
        windTextView.setText("");
        humidityTextView.setText("");
    }

    public void setWeatherManager(WeatherManager weatherManager) {
        this.weatherManager = weatherManager;
    }
}