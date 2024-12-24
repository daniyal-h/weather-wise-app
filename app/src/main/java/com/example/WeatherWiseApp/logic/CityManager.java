package com.example.WeatherWiseApp.logic;

import com.example.WeatherWiseApp.objects.City;

import java.util.HashMap;
import java.util.Map;

public class CityManager implements ICityManager {
    private Map<String, City> cities = new HashMap<>();

    public void addCity(City city) {
        cities.put(city.getCity(), city);
    }

    @Override
    public void removeCity(String cityName) {
        cities.remove(cityName);
    }

    @Override
    public City getCity(String cityName) {
        return cities.get(cityName);
    }

    @Override
    public boolean cityExists(String cityName) {
        return cities.containsKey(cityName);
    }

    @Override
    public int getCityCount() {
        return cities.size();
    }

    @Override
    public void clearAllCities() {
        cities.clear();
    }
}
