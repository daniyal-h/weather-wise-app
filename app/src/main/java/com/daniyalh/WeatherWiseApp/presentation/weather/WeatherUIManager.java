package com.daniyalh.WeatherWiseApp.presentation.weather;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.objects.City;
import com.daniyalh.WeatherWiseApp.objects.Weather;

public class WeatherUIManager {
    private final WeatherPage weatherPage;
    private final Handler handler = new Handler();
    private Runnable updateTimeRunnable;

    private TextView cityTextView, descriptionTextView, forecastUpdateTextView,
            sunriseLabelTextView, sunsetLabelTextView, windLabelTextView, humidityLabelTextView,
            tempTextView, feelsLikeTextView, sunriseTextView, sunsetTextView, windTextView, humidityTextView;

    private LottieAnimationView loadingIconLottie, sunIconLottie, moonIconLottie, windIconLottie, humidityIconLottie,
            favouritingAnimationButton, extendedForecastAnimationButton;

    private Button goBackButton;

    private boolean isFavourite;

    public WeatherUIManager(WeatherPage weatherPage, boolean isFavourite) {
        this.weatherPage = weatherPage;
        initializeUI();
        setButtonListeners();
        this.isFavourite = isFavourite;
    }

    public void initializeUI() {
        cityTextView = weatherPage.findViewById(R.id.city_text_view);
        descriptionTextView = weatherPage.findViewById(R.id.description_text_view);
        forecastUpdateTextView = weatherPage.findViewById(R.id.forecast_update_text_view);

        sunriseLabelTextView = weatherPage.findViewById(R.id.sunrise_label_text_view);
        sunsetLabelTextView = weatherPage.findViewById(R.id.sunset_label_text_view);
        windLabelTextView = weatherPage.findViewById(R.id.wind_label_text_view);
        humidityLabelTextView = weatherPage.findViewById(R.id.humidity_label_text_view);

        goBackButton = weatherPage.findViewById(R.id.back_home_button);
        favouritingAnimationButton = weatherPage.findViewById(R.id.favourite_icon_lottie);
        extendedForecastAnimationButton = weatherPage.findViewById(R.id.extended_forecast_icon_lottie);

        tempTextView = weatherPage.findViewById(R.id.temperature_text_view);
        feelsLikeTextView = weatherPage.findViewById(R.id.feels_like_text_view);
        sunriseTextView = weatherPage.findViewById(R.id.sunrise_text_view);
        sunsetTextView = weatherPage.findViewById(R.id.sunset_text_view);
        windTextView = weatherPage.findViewById(R.id.wind_text_view);
        humidityTextView = weatherPage.findViewById(R.id.humidity_text_view);

        loadingIconLottie = weatherPage.findViewById(R.id.loading_weather_icon_lottie);
        sunIconLottie = weatherPage.findViewById(R.id.sun_icon_lottie);
        moonIconLottie = weatherPage.findViewById(R.id.moon_icon_lottie);
        windIconLottie = weatherPage.findViewById(R.id.wind_icon_lottie);
        humidityIconLottie = weatherPage.findViewById(R.id.humidity_icon_lottie);
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

    private void setButtonListeners() {
        favouritingAnimationButton.setOnClickListener(v -> {
            // toggle favourite depending on isFavourite
            // play proper icon
            if (isFavourite) {
                favouritingAnimationButton.setSpeed(-1f); // reverse animation
                favouritingAnimationButton.playAnimation();
                showToast("Unfavourited city", Toast.LENGTH_SHORT);
                isFavourite = false;
            }
            else {
                favouritingAnimationButton.playAnimation();
                favouritingAnimationButton.cancelAnimation();
                showToast("Favourited city", Toast.LENGTH_SHORT);
                isFavourite = true;
            }
            weatherPage.toggleFavourite(isFavourite); // toggle favourite
        });

        extendedForecastAnimationButton.setOnClickListener(v -> weatherPage.startForecast());

        goBackButton.setOnClickListener(v -> weatherPage.finish()); // home page
    }

    public void setStaticUIVisibility(boolean visible) {
        // make the static UI elements invisible or visible
        int visibility = visible ? View.VISIBLE : View.INVISIBLE;

        forecastUpdateTextView.setVisibility(visibility);
        sunriseLabelTextView.setVisibility(visibility);
        sunsetLabelTextView.setVisibility(visibility);
        windLabelTextView.setVisibility(visibility);
        humidityLabelTextView.setVisibility(visibility);

        sunIconLottie.setVisibility(visibility);
        moonIconLottie.setVisibility(visibility);
        windIconLottie.setVisibility(visibility);
        humidityIconLottie.setVisibility(visibility);
        favouritingAnimationButton.setVisibility(visibility);
        extendedForecastAnimationButton.setVisibility(visibility);
        if (isFavourite) favouritingAnimationButton.playAnimation();

        // stop or play the animations to reduce memory usage
        if (visible) {
            sunIconLottie.playAnimation();
            moonIconLottie.playAnimation();
            windIconLottie.playAnimation();
            humidityIconLottie.playAnimation();
            extendedForecastAnimationButton.playAnimation();
        }
        else { // invisible icons shouldn't be playing
            sunIconLottie.cancelAnimation();
            moonIconLottie.cancelAnimation();
            windIconLottie.cancelAnimation();
            humidityIconLottie.cancelAnimation();
            favouritingAnimationButton.cancelAnimation();
            extendedForecastAnimationButton.cancelAnimation();
        }
    }

    public void updateWeatherDetails(City city) {
        showLoadingIcon(false);
        setCityLabel(city);
        setStaticUIVisibility(true);
        updateWeatherDetails(city.getCurrentWeather());
    }

    private void setCityLabel(City city) {
        // set as "city, countryCode" and time as update time
        String displayName = city.getCityName().toUpperCase() + ", " + city.getCountryCode();
        cityTextView.setText(displayName);
    }

    private void updateWeatherDetails(Weather weather) {
        // set the weather details to their corresponding UI elements
        Long lastUpdatedTime = weather.getLastUpdated();
        String temp = (int) weather.getTemp() + "°C";
        String feelsLike = "Feels Like " + (int) weather.getFeelsLike();
        String description = weather.getDescription();
        String humidity = weather.getHumidity() + "%";
        String windSpeed = (int) weather.getWindSpeed() + " km/h";
        String sunrise = weather.getSunrise().toLowerCase();
        String sunset = weather.getSunset().toUpperCase();

        tempTextView.setText(temp);
        feelsLikeTextView.setText(feelsLike);
        descriptionTextView.setText(description);
        humidityTextView.setText(humidity);
        windTextView.setText(windSpeed);
        sunriseTextView.setText(sunrise);
        sunsetTextView.setText(sunset);
        setTimeOfDay(weather.getTimeOfDay());

        // update the last updated time through a thread every minute
        startUpdatingTime(lastUpdatedTime);
    }

    private void setTimeOfDay(char timeOfDay) {
        // set the background colour depending on the time of day
        if (timeOfDay == 'd') {
            weatherPage.findViewById(R.id.root_layout).
                    setBackground(ContextCompat.getDrawable(weatherPage,
                            R.drawable.day_gradient_background));
        } else { // nighttime
            weatherPage.findViewById(R.id.root_layout).
                    setBackground(ContextCompat.getDrawable(weatherPage,
                            R.drawable.night_gradient_background));
        }
    }

    public void startUpdatingTime(long lastUpdatedTime) {
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                updateForecastTime(lastUpdatedTime);
                handler.postDelayed(this, 60000); // Update every 60 seconds
            }
        };
        handler.post(updateTimeRunnable); // Start the runnable
    }

    public void stopUpdatingTime() {
        if (updateTimeRunnable != null) {
            handler.removeCallbacks(updateTimeRunnable);
        }
    }

    public void resumeUpdatingTime() {
        if (updateTimeRunnable != null) {
            handler.post(updateTimeRunnable);
        }
    }

    private void updateForecastTime(Long lastUpdatedTime) {
        // update the update text view with minutes since update
        long currentTime = System.currentTimeMillis();
        int minutesAgo = (int) (currentTime - lastUpdatedTime) / (60 * 1000); // Convert milliseconds to minutes
        String updateTime = (minutesAgo <= 1) ? "Updated just now" : "Updated " + minutesAgo + " minutes ago";
        forecastUpdateTextView.setText(updateTime);
    }

    private void resetUI() {
        setStaticUIVisibility(false);
        cityTextView.setText("");
        forecastUpdateTextView.setText("");
        tempTextView.setText("");
        feelsLikeTextView.setText("");
        descriptionTextView.setText("");
        sunriseTextView.setText("");
        sunsetTextView.setText("");
        windTextView.setText("");
        humidityTextView.setText("");
    }

    public void cleanup() {
        resetUI();
        stopUpdatingTime();
    }

    public void showToast(String message, int duration) {
        Toast.makeText(weatherPage, message, duration).show();
    }
}