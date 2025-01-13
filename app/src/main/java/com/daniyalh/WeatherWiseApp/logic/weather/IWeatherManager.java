package com.daniyalh.WeatherWiseApp.logic.weather;

import com.daniyalh.WeatherWiseApp.objects.City;

public interface IWeatherManager {
    void getWeatherFromDB(City city, IWeatherDetailsCallback callback);
    void getWeatherJSON(City city, IWeatherCallback callback);
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
