package com.example.WeatherWiseApp.logic;

import com.example.WeatherWiseApp.objects.City;

public interface ICityManager {
    void addCity(City city);
    void removeCity(String cityName);
    City getCity(String cityName);
    boolean cityExists(String cityName);
    int getCityCount();
    void clearAllCities();
}
