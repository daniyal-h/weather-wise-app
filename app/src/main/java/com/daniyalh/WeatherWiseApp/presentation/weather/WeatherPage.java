package com.daniyalh.WeatherWiseApp.presentation.weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.logic.weather.FavouritesManager;
import com.daniyalh.WeatherWiseApp.logic.weather.WeatherManager;
import com.daniyalh.WeatherWiseApp.objects.City;
import com.daniyalh.WeatherWiseApp.presentation.UIConstants;
import com.daniyalh.WeatherWiseApp.presentation.forecast.ForecastPage;

public class WeatherPage extends AppCompatActivity {
    private WeatherController weatherController;
    private WeatherUIManager weatherUIManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);

        // get all parameters
        Intent intent = getIntent();
        int cityID = intent.getIntExtra(UIConstants.EXTRA_CITY_ID, 0);
        String cityName = intent.getStringExtra(UIConstants.EXTRA_CITY_NAME);
        String countryCode = intent.getStringExtra(UIConstants.EXTRA_COUNTRY_CODE);
        boolean isFavourite = intent.getIntExtra(UIConstants.EXTRA_IS_FAVOURITE, 0) == 1;

        initializeClasses(isFavourite);

        // display the loading icon while fetching weather asynchronously
        showLoadingIcon(true);
        weatherController.fetchWeather(cityID, cityName, countryCode); // forecast
    }

    private void initializeClasses(boolean isFavourite) {
        FavouritesManager favouritesManager = FavouritesManager.getInstance();
        WeatherManager weatherManager = new WeatherManager(this);
        weatherController = WeatherController.getInstance();
        weatherController.injectDependencies(this, weatherManager, favouritesManager);

        weatherUIManager = new WeatherUIManager(this, isFavourite);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close this activity and return to parent
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showLoadingIcon(boolean visible) {
        weatherUIManager.showLoadingIcon(visible);
    }

    public void updateWeatherDetails(City city) {
        weatherUIManager.updateWeatherDetails(city);
    }

    public void toggleFavourite(boolean isFavourite) {
        weatherController.toggleFavourite(isFavourite);
    }

    public void showToast(String message, int duration) {
        weatherUIManager.showToast(message, duration);
    }

    public void startForecast() {
        startActivity(new Intent(WeatherPage.this, ForecastPage.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        weatherUIManager.stopUpdatingTime(); // Stop updates when the activity is not visible
    }

    @Override
    protected void onResume() {
        super.onResume();
        weatherUIManager.resumeUpdatingTime(); // Resume updates when the activity comes back to the foreground
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        weatherUIManager.cleanup(); // Clean up the runnable
    }
}