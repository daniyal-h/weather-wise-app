package com.daniyalh.WeatherWiseApp.logic;

import com.daniyalh.WeatherWiseApp.objects.City;
import com.daniyalh.WeatherWiseApp.objects.Forecast;

public interface IForecastManager {
    void getForecastJSON(City city, IForecastCallback forecastCallback);


    interface IForecastCallback {
        void onSuccess(Forecast[] forecasts);

        void onError(String error);
    }
}
