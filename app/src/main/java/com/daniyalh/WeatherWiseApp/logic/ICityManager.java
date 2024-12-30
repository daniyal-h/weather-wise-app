package com.daniyalh.WeatherWiseApp.logic;

import com.daniyalh.WeatherWiseApp.objects.City;

public interface ICityManager {
    void addCity(City city);
    void removeCity(int cityID);
    City getCity(int cityID);
    boolean cityExists(int cityID);
    int getCityCount();
    void clearAllCities();

    //void favouriteCity(City city);

    void unfavouriteCity(City city);

    City getFavouritedCity(int cityID);

    boolean isFavourite(City city);
}
