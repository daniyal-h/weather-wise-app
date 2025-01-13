package com.daniyalh.WeatherWiseApp.logic.weather;

import com.daniyalh.WeatherWiseApp.objects.Forecast;

public interface IWeatherJsonAdapter {
    String[] parseWeather(String weatherJSON);
}
