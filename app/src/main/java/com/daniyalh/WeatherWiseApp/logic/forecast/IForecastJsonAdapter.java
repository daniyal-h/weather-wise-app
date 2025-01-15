package com.daniyalh.WeatherWiseApp.logic.forecast;

import com.daniyalh.WeatherWiseApp.objects.Forecast;

public interface IForecastJsonAdapter {
    Forecast[] parseForecast(String response);
}
