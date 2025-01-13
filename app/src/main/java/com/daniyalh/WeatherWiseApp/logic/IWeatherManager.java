package com.daniyalh.WeatherWiseApp.logic;

import com.daniyalh.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;

import com.daniyalh.WeatherWiseApp.objects.City;
import com.daniyalh.WeatherWiseApp.objects.CityWeather;

public interface IWeatherManager {
    void getWeatherJSON(City city, IWeatherCallback callback);
    void setWeather(CityWeather city, String weatherJSON) throws InvalidJsonParsingException;
    void getWeatherFromDB(City city, IWeatherDetailsCallback callback);
    String[] fetchImmediateWeather(String weatherJSON);

    interface IWeatherCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    interface IWeatherDetailsCallback {
        void onSuccess(String[] weatherDetails);

        void onError(String error);
    }
}
