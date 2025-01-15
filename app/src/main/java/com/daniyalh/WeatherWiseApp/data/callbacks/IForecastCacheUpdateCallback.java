package com.daniyalh.WeatherWiseApp.data.callbacks;

public interface IForecastCacheUpdateCallback {
    void onCacheUpdated(String forecastJSON);
    void onError(String error);
}
