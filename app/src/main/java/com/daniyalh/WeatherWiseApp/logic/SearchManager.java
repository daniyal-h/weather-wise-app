package com.daniyalh.WeatherWiseApp.logic;

import com.daniyalh.WeatherWiseApp.data.MyDatabaseHelper;

public class SearchManager implements ISearchManager {
    private final MyDatabaseHelper myDatabase;

    public SearchManager(MyDatabaseHelper myDatabase) {
        this.myDatabase = myDatabase;
    }
    @Override
    public void searchCities(String query, SearchCallback callback) {
        // return a cursor containing all matching cities to query
        if (query == null || query.trim().isEmpty()) {
            callback.onError("please enter a city");
            return;
        }

        // pass the cursor to the callback
        callback.onResults(myDatabase.getCitiesByQuery(query));
    }
}
