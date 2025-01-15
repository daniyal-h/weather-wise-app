package com.daniyalh.WeatherWiseApp.logic.forecast;

import com.daniyalh.WeatherWiseApp.objects.City;
import com.daniyalh.WeatherWiseApp.objects.Forecast;

public interface IForecastManager {
    public void getForecastFromDB(City city, IForecastDetailsCallback callback);
    void getForecastJSON(City city, IForecastCallback forecastCallback);
    Forecast[] parseJSON(String forecastJSON);

    interface IForecastCallback {
        void onSuccess(String forecastJSON);
        void onError(String error);
    }

    interface IForecastDetailsCallback {
        void onSuccess(Forecast[] forecasts);
        void onError(String error);
    }
}
