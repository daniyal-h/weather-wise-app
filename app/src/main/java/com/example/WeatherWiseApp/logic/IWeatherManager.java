package com.example.WeatherWiseApp.logic;

import android.content.Context;

import com.example.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;
import com.example.WeatherWiseApp.objects.City;

public interface IWeatherManager {
    void getWeatherJSON(City city, final IWeatherCallback callback);
    void setWeather(City city, String weatherJSON) throws InvalidJsonParsingException;
}
