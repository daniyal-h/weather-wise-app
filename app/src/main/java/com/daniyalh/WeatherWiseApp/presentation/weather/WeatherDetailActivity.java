package com.daniyalh.WeatherWiseApp.presentation.weather;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.logic.weather.FavouritesManager;
import com.daniyalh.WeatherWiseApp.logic.weather.WeatherManager;
import com.daniyalh.WeatherWiseApp.objects.City;
import com.daniyalh.WeatherWiseApp.objects.Weather;
import com.daniyalh.WeatherWiseApp.presentation.UIConstants;
import com.daniyalh.WeatherWiseApp.presentation.forecast.ForecastDetailActivity;

public class WeatherDetailActivity extends AppCompatActivity {
    private WeatherController weatherController;
    private final Handler handler = new Handler();
    private Runnable updateTimeRunnable;

    private TextView cityTextView, descriptionTextView, forecastUpdateTextView,
            sunriseLabelTextView, sunsetLabelTextView, windLabelTextView, humidityLabelTextView,
            tempTextView, feelsLikeTextView, sunriseTextView, sunsetTextView, windTextView, humidityTextView;

    private LottieAnimationView loadingIconLottie, sunIconLottie, moonIconLottie, windIconLottie, humidityIconLottie,
            favouritingAnimationButton, extendedForecastAnimationButton;

    private Button goBackButton;

    private boolean isFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);

        initializeUI();

        // get all parameters
        Intent intent = getIntent();
        int cityID = intent.getIntExtra(UIConstants.EXTRA_CITY_ID, 0);
        String cityName = intent.getStringExtra(UIConstants.EXTRA_CITY_NAME);
        String countryCode = intent.getStringExtra(UIConstants.EXTRA_COUNTRY_CODE);
        isFavourite = intent.getIntExtra(UIConstants.EXTRA_IS_FAVOURITE, 0) == 1;

        initializeClasses();

        // display the loading icon while fetching weather asynchronously
        showLoadingIcon(true);
        weatherController.fetchWeather(cityID, cityName, countryCode); // forecast

        setButtonListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close this activity and return to parent
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeUI() {
        cityTextView = findViewById(R.id.city_text_view);
        descriptionTextView = findViewById(R.id.description_text_view);
        forecastUpdateTextView = findViewById(R.id.forecast_update_text_view);

        sunriseLabelTextView = findViewById(R.id.sunrise_label_text_view);
        sunsetLabelTextView = findViewById(R.id.sunset_label_text_view);
        windLabelTextView = findViewById(R.id.wind_label_text_view);
        humidityLabelTextView = findViewById(R.id.humidity_label_text_view);

        goBackButton = findViewById(R.id.back_home_button);
        favouritingAnimationButton = findViewById(R.id.favourite_icon_lottie);
        extendedForecastAnimationButton = findViewById(R.id.extended_forecast_icon_lottie);

        tempTextView = findViewById(R.id.temperature_text_view);
        feelsLikeTextView = findViewById(R.id.feels_like_text_view);
        sunriseTextView = findViewById(R.id.sunrise_text_view);
        sunsetTextView = findViewById(R.id.sunset_text_view);
        windTextView = findViewById(R.id.wind_text_view);
        humidityTextView = findViewById(R.id.humidity_text_view);

        loadingIconLottie = findViewById(R.id.loading_weather_icon_lottie);
        sunIconLottie = findViewById(R.id.sun_icon_lottie);
        moonIconLottie = findViewById(R.id.moon_icon_lottie);
        windIconLottie = findViewById(R.id.wind_icon_lottie);
        humidityIconLottie = findViewById(R.id.humidity_icon_lottie);
    }

    private void initializeClasses() {
        FavouritesManager favouritesManager = FavouritesManager.getInstance();
        WeatherManager weatherManager = new WeatherManager(this);
        weatherController = WeatherController.getInstance();
        weatherController.injectDependencies(this, weatherManager, favouritesManager);
    }

    public void setCityLabel(City city) {
        // set as "city, countryCode" and time as update time
        String displayName = city.getCityName().toUpperCase() + ", " + city.getCountryCode();
        cityTextView.setText(displayName);
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
                showToast("Favourited city", Toast.LENGTH_SHORT);
                isFavourite = true;
            }
            weatherController.toggleFavourite(isFavourite); // toggle favourite
        });

        extendedForecastAnimationButton.setOnClickListener(v ->
                // make an extended forecast page
                startActivity(new Intent(WeatherDetailActivity.this, ForecastDetailActivity.class)));

        goBackButton.setOnClickListener(v -> finish()); // home page
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

    public void updateWeatherDetails(Weather weather) {
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
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                updateForecastTime(lastUpdatedTime);
                handler.postDelayed(this, 60000); // Update every 60 seconds
            }
        };
        handler.post(updateTimeRunnable); // Start the runnable
    }

    private void setTimeOfDay(char timeOfDay) {
        // set the background colour depending on the time of day
        if (timeOfDay == 'd') {
            findViewById(R.id.root_layout).setBackgroundColor
                    (ContextCompat.getColor(this, R.color.day_background));
        } else { // nighttime
            findViewById(R.id.root_layout).setBackgroundColor
                    (ContextCompat.getColor(this, R.color.night_background));
        }
    }

    private void updateForecastTime(Long lastUpdatedTime) {
        // update the update text view with minutes since update
        long currentTime = System.currentTimeMillis();
        int minutesAgo = (int) (currentTime - lastUpdatedTime) / (60 * 1000); // Convert milliseconds to minutes
        String updateTime = (minutesAgo <= 1) ? "Updated just now" : "Updated " + minutesAgo + " minutes ago";
        forecastUpdateTextView.setText(updateTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateTimeRunnable); // Stop updates when the activity is not visible
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(updateTimeRunnable); // Resume updates when the activity comes back to the foreground
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resetUI();
        handler.removeCallbacks(updateTimeRunnable); // Clean up the runnable
    }

    public void resetUI() {
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

    public void showToast(String message, int duration) {
        Toast.makeText(this, message, duration).show();
    }

    public void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}