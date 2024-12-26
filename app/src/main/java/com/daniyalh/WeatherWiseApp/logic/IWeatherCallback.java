package com.daniyalh.WeatherWiseApp.logic;

public interface IWeatherCallback {
    void onSuccess(String response);
    void onError(String error);
}