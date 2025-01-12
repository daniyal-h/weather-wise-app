package com.daniyalh.WeatherWiseApp.presentation;

import com.daniyalh.WeatherWiseApp.logic.FavouritesManager;
import com.daniyalh.WeatherWiseApp.logic.IWeatherManager;
import com.daniyalh.WeatherWiseApp.logic.WeatherManager;
import com.daniyalh.WeatherWiseApp.objects.City;
import com.daniyalh.WeatherWiseApp.objects.CityWeather;
import com.daniyalh.WeatherWiseApp.objects.Forecast;
import com.daniyalh.WeatherWiseApp.objects.Weather;

public class WeatherController {
    private static final String TAG = "WeatherController";
    private static WeatherController instance;

    private WeatherManager weatherManager;
    private FavouritesManager favouritesManager;
    private WeatherDetailActivity context;

    private CityWeather cityWeather;
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

    public void injectDependencies(WeatherManager weatherManager,
                                   FavouritesManager favouritesManager,
                                   WeatherDetailActivity context) {
        this.weatherManager = weatherManager;
        this.favouritesManager = favouritesManager;
        this.context = context;
    }

    public void fetchWeather(int cityID, String cityName, String country, String countryCode) {
        cityWeather = new CityWeather(cityID);
        cityWeather.setCountry(cityName, country, countryCode);

        city = new City(cityID);
        city.setDetails(cityName, country, countryCode);

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

            }
        });

        /*
        weatherManager.getWeatherFromDB(cityWeather, new IWeatherManager.IWeatherDetailsCallback() {
            @Override
            public void onSuccess(String[] weatherDetails) {
                context.showLoadingIcon(false);
                cityWeather.updateWeather(weatherDetails);
                context.setCityLabel(cityWeather);

                context.setStaticUIVisibility(true);
                context.updateWeatherDetails(cityWeather.getWeather());
            }

            @Override
            public void onError(String error) {
                context.showLoadingIcon(false);
                context.showAlertDialog("Error", "Unable to fetch weather data." +
                        " Please check the city name and try again.");
                Log.e(TAG, "Error fetching weather for city " + cityWeather.getCity() + " - " + error);
                context.resetUI();
            }
        });*/
    }

    public void fetchExtendedWeather() {
        Forecast[] forecasts = new Forecast[40];
    }

    public void toggleFavourite(boolean isFavourite) {
        favouritesManager.toggleFavourite(cityWeather.getCityID(), isFavourite);
    }
}