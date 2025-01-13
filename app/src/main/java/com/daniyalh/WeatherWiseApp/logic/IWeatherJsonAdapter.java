package com.daniyalh.WeatherWiseApp.logic;

import com.daniyalh.WeatherWiseApp.objects.Forecast;

public interface IWeatherJsonAdapter {
    String[] parseWeather(String weatherJSON);

    Forecast[] parseForecast(String response);
}
