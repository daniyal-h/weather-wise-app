package com.daniyalh.WeatherWiseApp.data;

public interface ICacheUpdateCallback {
    void onCacheUpdated(String[] newWeatherData);
    void onError(String error);
}
