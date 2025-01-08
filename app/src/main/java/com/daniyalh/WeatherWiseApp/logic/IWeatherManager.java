package com.daniyalh.WeatherWiseApp.logic;

import com.daniyalh.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;

import com.daniyalh.WeatherWiseApp.objects.City;

public interface IWeatherManager {
    void getWeatherJSON(City city, final IWeatherCallback callback);
    void setWeather(City city, String weatherJSON) throws InvalidJsonParsingException;
}
