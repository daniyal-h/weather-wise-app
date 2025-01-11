package com.daniyalh.WeatherWiseApp.presentation;

import android.util.Log;
import android.widget.Toast;

import com.daniyalh.WeatherWiseApp.logic.FavouritesManager;
import com.daniyalh.WeatherWiseApp.logic.IWeatherCallback;
import com.daniyalh.WeatherWiseApp.logic.IWeatherManager;
import com.daniyalh.WeatherWiseApp.logic.WeatherManager;
import com.daniyalh.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;
import com.daniyalh.WeatherWiseApp.objects.City;

public class WeatherController {
    private static final String TAG = "WeatherController";

    private final WeatherManager weatherManager;
    private final FavouritesManager favouritesManager;
    private final ForecastDetailActivity context;

    private City city;

    public WeatherController(WeatherManager weatherManager, FavouritesManager favouritesManager, ForecastDetailActivity context) {
        this.weatherManager = weatherManager;
        //this.cityManager = cityManager;
        this.favouritesManager = favouritesManager;
        this.context = context;
    }

    public void fetchWeather(int cityID, String cityName, String country, String country_code) {
        city = new City(cityID);
        city.setCountry(cityName, country, country_code);

        weatherManager.getWeatherFromDB(city, new IWeatherManager.IWeatherDetailsCallback() {
            @Override
            public void onSuccess(String[] weatherDetails) {
                context.showLoadingIcon(false);
                city.updateWeather(weatherDetails);
                context.setCityLabel(city);

                context.setStaticUIVisibility(true);
                context.updateWeatherDetails(city.getWeather());
            }

            @Override
            public void onError(String error) {
                context.showLoadingIcon(false);
                context.showAlertDialog("Error", "Unable to fetch weather data." +
                        " Please check the city name and try again.");
                Log.e(TAG, "Error fetching weather for city " + city.getCity() + " - " + error);
                context.resetUI();
            }
        });
        /*
        weatherManager.getWeatherJSON(city, new IWeatherManager.IWeatherCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    context.showLoadingIcon(false);
                    weatherManager.setWeather(city, response);
                    context.setCityLabel(city);

                    context.setStaticUIVisibility(true);
                    context.updateWeatherDetails(city.getWeather());

                    //cityManager.addCity(city); // Add or update record
                } catch (InvalidJsonParsingException e) {
                    context.showToast(e.getMessage(), Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onError(String error) {
                context.showLoadingIcon(false);
                context.showAlertDialog("Error", "Unable to fetch weather data." +
                        " Please check the city name and try again.");
                Log.e(TAG, "Error fetching weather for city " + city.getCity() + " - " + error);
                context.resetUI();
            }
        });
         */
    }

    public void toggleFavourite(boolean isFavourite) {
        favouritesManager.toggleFavourite(city.getCityID(), isFavourite);
    }
}