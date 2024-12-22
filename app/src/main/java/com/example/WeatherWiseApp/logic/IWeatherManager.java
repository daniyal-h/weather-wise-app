package com.example.WeatherWiseApp.logic;

import android.content.Context;

import com.example.WeatherWiseApp.objects.City;

public interface IWeatherManager {
    void getWeatherJSON(Context context, City city, final IWeatherCallback callback);
    void setWeather(City city, String weatherJSON);
}
