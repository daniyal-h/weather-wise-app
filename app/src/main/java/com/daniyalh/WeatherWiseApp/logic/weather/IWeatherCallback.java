package com.daniyalh.WeatherWiseApp.logic.weather;

public interface IWeatherCallback {
    void onSuccess(String response);
    void onError(String error);
}