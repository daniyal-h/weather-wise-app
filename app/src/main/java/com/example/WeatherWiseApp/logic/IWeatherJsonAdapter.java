package com.example.WeatherWiseApp.logic;

import com.example.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;

public interface IWeatherJsonAdapter {
    String[] parseWeather(String weatherJSON);
}
