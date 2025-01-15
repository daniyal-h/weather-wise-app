package com.daniyalh.WeatherWiseApp.logic;

import com.daniyalh.WeatherWiseApp.data.DatabaseHelper;

public class SearchManager implements ISearchManager {
    private final DatabaseHelper dhHelper;

    public SearchManager(DatabaseHelper dhHelper) {
        this.dhHelper = dhHelper;
    }
    @Override
    public void searchCities(String query, SearchCallback callback) {
        // return a cursor containing all matching cities to query
        if (query == null || query.trim().isEmpty()) {
            callback.onError("please enter a city");
            return;
        }

        // pass the cursor to the callback
        callback.onResults(dhHelper.getCityRepository().getCitiesByQuery(query));
    }
}
