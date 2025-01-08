package com.daniyalh.WeatherWiseApp.presentation;

import android.content.Intent;
import android.os.Bundle;
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
import com.daniyalh.WeatherWiseApp.logic.CityManager;
import com.daniyalh.WeatherWiseApp.logic.FavouritesManager;
import com.daniyalh.WeatherWiseApp.logic.WeatherManager;
import com.daniyalh.WeatherWiseApp.objects.City;

import java.io.Serializable;

public class ForecastDetailActivity extends AppCompatActivity {
    private WeatherController weatherController;

    private TextView cityTextView, descriptionTextView,
            sunriseLabelTextView, sunsetLabelTextView, windLabelTextView, humidityLabelTextView,
            tempTextView, feelsLikeTextView, sunriseTextView, sunsetTextView, windTextView, humidityTextView;

    private LottieAnimationView loadingIconLottie, sunIconLottie, moonIconLottie, windIconLottie, humidityIconLottie,
    favouritingAnimation;

    private Button goBackButton;

    private boolean isFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_detail);

        initializeUI();

        Intent intent = getIntent();
        int cityID = intent.getIntExtra(UIConstants.EXTRA_CITY_ID, 0);
        String cityName = intent.getStringExtra(UIConstants.EXTRA_CITY_NAME);
        String countryName = intent.getStringExtra(UIConstants.EXTRA_COUNTRY_NAME);
        String countryCode = intent.getStringExtra(UIConstants.EXTRA_COUNTRY_CODE);
        isFavourite = intent.getIntExtra(UIConstants.EXTRA_IS_FAVOURITE, 0) == 1;

        initializeClasses();

        // display the loading icon while fetching weather asynchronously
        showLoadingIcon(true);
        weatherController.fetchWeather(cityID, cityName, countryName, countryCode);

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

        sunriseLabelTextView = findViewById(R.id.sunrise_label_text_view);
        sunsetLabelTextView = findViewById(R.id.sunset_label_text_view);
        windLabelTextView = findViewById(R.id.wind_label_text_view);
        humidityLabelTextView = findViewById(R.id.humidity_label_text_view);

        goBackButton = findViewById(R.id.clear_favourites_button);
        favouritingAnimation = findViewById(R.id.favourite_icon_lottie);

        tempTextView = findViewById(R.id.temperature_text_view);
        feelsLikeTextView = findViewById(R.id.feels_like_text_view);
        sunriseTextView = findViewById(R.id.sunrise_text_view);
        sunsetTextView = findViewById(R.id.sunset_text_view);
        windTextView = findViewById(R.id.wind_text_view);
        humidityTextView = findViewById(R.id.humidity_text_view);

        loadingIconLottie = findViewById(R.id.loading_icon_lottie);
        sunIconLottie = findViewById(R.id.sun_icon_lottie);
        moonIconLottie = findViewById(R.id.moon_icon_lottie);
        windIconLottie = findViewById(R.id.wind_icon_lottie);
        humidityIconLottie = findViewById(R.id.humidity_icon_lottie);
    }

    private void initializeClasses() {
        FavouritesManager favouritesManager = FavouritesManager.getInstance(null);
        WeatherManager weatherManager = new WeatherManager(this);
        CityManager cityManager = new CityManager();
        weatherController = new WeatherController(weatherManager, favouritesManager, this);
    }

    public void setCityLabel(City city) {
        // set as "city, countryCode"
        cityTextView.setText(city.getCity().toUpperCase() + ", " + city.getCountryCode());
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
        favouritingAnimation.setOnClickListener(v -> {
            if (isFavourite) {
                favouritingAnimation.setSpeed(-1f);
                favouritingAnimation.playAnimation();
                showToast("Unfavourited city", Toast.LENGTH_SHORT);
                isFavourite = false;
            }
            else {
                favouritingAnimation.playAnimation();
                showToast("Favourited city", Toast.LENGTH_SHORT);
                isFavourite = true;
            }
            weatherController.toggleFavourite(isFavourite); // toggle favourite
        });

        goBackButton.setOnClickListener(v -> finish()); // home page
    }

    public void setStaticUIVisibility(boolean visible) {
        // make the static UI elements invisible or visible
        int visibility = visible ? View.VISIBLE : View.INVISIBLE;

        sunriseLabelTextView.setVisibility(visibility);
        sunsetLabelTextView.setVisibility(visibility);
        windLabelTextView.setVisibility(visibility);
        humidityLabelTextView.setVisibility(visibility);

        sunIconLottie.setVisibility(visibility);
        moonIconLottie.setVisibility(visibility);
        windIconLottie.setVisibility(visibility);
        humidityIconLottie.setVisibility(visibility);
        favouritingAnimation.setVisibility(visibility);
        if (isFavourite) favouritingAnimation.playAnimation();

        // stop or play the animations to reduce memory usage
        if (visible) {
            sunIconLottie.playAnimation();
            moonIconLottie.playAnimation();
            windIconLottie.playAnimation();
            humidityIconLottie.playAnimation();
        }
        else { // invisible icons shouldn't be playing
            sunIconLottie.cancelAnimation();
            moonIconLottie.cancelAnimation();
            windIconLottie.cancelAnimation();
            humidityIconLottie.cancelAnimation();
            favouritingAnimation.cancelAnimation();
        }
    }

    public void updateWeatherDetails(String[] details) {
        tempTextView.setText(details[0]);
        feelsLikeTextView.setText(details[1]);
        descriptionTextView.setText(details[2]);
        humidityTextView.setText(details[3]);
        windTextView.setText(details[4]);
        sunriseTextView.setText(details[5]);
        sunsetTextView.setText(details[6]);
        setTimeOfDay(details[7].charAt(0));
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

    public void resetUI() {
        setStaticUIVisibility(false);
        cityTextView.setText("");
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