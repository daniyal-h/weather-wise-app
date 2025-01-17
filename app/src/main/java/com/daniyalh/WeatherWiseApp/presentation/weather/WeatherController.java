package com.daniyalh.WeatherWiseApp.presentation.weather;

import android.util.Log;

import com.daniyalh.WeatherWiseApp.logic.weather.FavouritesManager;
import com.daniyalh.WeatherWiseApp.logic.weather.IWeatherManager;
import com.daniyalh.WeatherWiseApp.logic.weather.WeatherManager;
import com.daniyalh.WeatherWiseApp.objects.City;
import com.daniyalh.WeatherWiseApp.objects.Weather;

public class WeatherController {
    private static final String TAG = "WeatherController";
    private static WeatherController instance;

    private WeatherManager weatherManager;
    private FavouritesManager favouritesManager;
    private WeatherPage context;
    private City city;
    private Weather weather;

    private WeatherController() {}

    public static WeatherController getInstance() {
        // singleton
        if (instance == null) {
            instance = new WeatherController();
        }
        return instance;
    }

    public void injectDependencies(WeatherPage context,
                                   WeatherManager weatherManager,
                                   FavouritesManager favouritesManager) {
        this.weatherManager = weatherManager;
        this.favouritesManager = favouritesManager;
        this.context = context;
    }

    public void fetchWeather(int cityID, String cityName, String countryCode) {
        city = new City(cityID);
        city.setDetails(cityName, countryCode);

        weatherManager.getWeatherFromDB(city, new IWeatherManager.IWeatherDetailsCallback() {
            @Override
            public void onSuccess(String[] weatherDetails) {
                context.showLoadingIcon(false);
                weather = new Weather(weatherDetails);
                city.setWeather(weather);
                context.setCityLabel(city);
                context.setStaticUIVisibility(true);
                context.updateWeatherDetails(weather);
            }

            @Override
            public void onError(String error) {
                // TODO
                Log.d("WeatherController", "Failed to fetch weather, error: " + error);
            }
        });
    }

    public void toggleFavourite(boolean isFavourite) {
        favouritesManager.toggleFavourite(city.getCityID(), isFavourite);
    }

    public City getCity() {
        return city;
    }
}