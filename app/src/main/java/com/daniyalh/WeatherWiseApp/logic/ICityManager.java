package com.daniyalh.WeatherWiseApp.logic;

import com.daniyalh.WeatherWiseApp.objects.City;

public interface ICityManager {
    void addCity(City city);
    void removeCity(String cityName);
    City getCity(String cityName);
    boolean cityExists(String cityName);
    int getCityCount();
    void clearAllCities();
}
