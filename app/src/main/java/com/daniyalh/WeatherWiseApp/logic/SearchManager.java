package com.daniyalh.WeatherWiseApp.logic;

import android.database.Cursor;

import com.daniyalh.WeatherWiseApp.data.MyDatabaseHelper;

public class SearchManager implements ISearchManager {
    private final MyDatabaseHelper myDatabase;

    public SearchManager(MyDatabaseHelper myDatabase) {
        this.myDatabase = myDatabase;
    }
    @Override
    public void searchCities(String query, SearchCallback callback) {
        if (query == null || query.trim().isEmpty()) {
            callback.onError("Query cannot be empty");
            return;
        }

        // Fetch results from the data layer
        Cursor cursor = myDatabase.getCitiesByQuery(query);

        if (cursor == null || cursor.getCount() == 0) {
            if (cursor != null) cursor.close();
            callback.onError("No results found");
        } else {
            callback.onResults(cursor);
        }
    }
}
