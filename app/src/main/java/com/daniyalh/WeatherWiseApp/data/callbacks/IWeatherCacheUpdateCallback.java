package com.daniyalh.WeatherWiseApp.data.callbacks;

public interface IWeatherCacheUpdateCallback {
    void onCacheUpdated(String[] newWeatherData);
    void onError(String error);
}
