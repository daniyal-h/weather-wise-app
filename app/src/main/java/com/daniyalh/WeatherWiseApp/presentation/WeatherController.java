package com.daniyalh.WeatherWiseApp.presentation;

import android.util.Log;
import android.widget.Toast;

import com.daniyalh.WeatherWiseApp.logic.CityManager;
import com.daniyalh.WeatherWiseApp.logic.IWeatherCallback;
import com.daniyalh.WeatherWiseApp.logic.WeatherManager;
import com.daniyalh.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;
import com.daniyalh.WeatherWiseApp.objects.City;

public class WeatherController {
    private static final String TAG = "WeatherController";

    private final WeatherManager weatherManager;
    private final CityManager cityManager;
    private final UIManager uiManager;

    public WeatherController(WeatherManager weatherManager, CityManager cityManager, UIManager uiManager) {
        this.weatherManager = weatherManager;
        this.cityManager = cityManager;
        this.uiManager = uiManager;
    }

    public void fetchWeather(String cityName, String country, String country_code) {
        City city = new City(cityName);
        city.setCountry(country, country_code);

        // display the loading icon while fetching weather asynchronously
        uiManager.showLoadingIcon(true);
        weatherManager.getWeatherJSON(city, new IWeatherCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    uiManager.showLoadingIcon(false);
                    weatherManager.setWeather(city, response);
                    uiManager.setCityLabel(city);

                    uiManager.setStaticUIVisibility(true);
                    uiManager.updateWeatherDetails(city.getWeather());

                    cityManager.addCity(city); // Add or update record
                } catch (InvalidJsonParsingException e) {
                    uiManager.showToast(e.getMessage(), Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onError(String error) {
                uiManager.showLoadingIcon(false);
                uiManager.showAlertDialog("Error", "Unable to fetch weather data." +
                        " Please check the city name and try again.");
                Log.e(TAG, "Error fetching weather for city " + city.getCity() + " - " + error);
                uiManager.resetUI();
            }
        });
    }
}