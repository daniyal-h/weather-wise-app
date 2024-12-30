package com.daniyalh.WeatherWiseApp.logic;

import com.daniyalh.WeatherWiseApp.data.MyDatabaseHelper;
import com.daniyalh.WeatherWiseApp.objects.City;

import java.util.HashMap;
import java.util.Map;

public class CityManager implements ICityManager {
    private static final Map<Integer, City> cities = new HashMap<>();
    private static final Map<Integer, City> favouriteCities = new HashMap<>();
    MyDatabaseHelper db;

    @Override
    public void addCity(City city) {
        cities.put(city.getCityID(), city);
    }

    @Override
    public void removeCity(int cityID) {
        cities.remove(cityID);
    }

    @Override
    public City getCity(int cityID) {
        return cities.get(cityID);
    }

    @Override
    public boolean cityExists(int cityID) {
        return cities.containsKey(cityID);
    }

    @Override
    public int getCityCount() {
        return cities.size();
    }

    @Override
    public void clearAllCities() {
        cities.clear();
    }

    //@Override
    public void favouriteCity(City city, boolean isFavourite) {
        //favouriteCities.put(city.getCityID(), city);
        db = MyDatabaseHelper.getInstance(null);
        db.updateFavouriteStatus(city.getCityID(), isFavourite);
    }

    @Override
    public void unfavouriteCity(City city) {
        favouriteCities.remove(city.getCityID());
    }

    @Override
    public City getFavouritedCity(int cityID) {
        return favouriteCities.get(cityID);
    }

    @Override
    public boolean isFavourite(City city) {
        return favouriteCities.containsKey(city.getCityID());
    }
}
