package com.daniyalh.WeatherWiseApp.logic;

import android.database.Cursor;

public interface ISearchManager {
    void searchCities(String query, SearchCallback callback);

    interface SearchCallback {
        void onResults(Cursor cursor);
        void onError(String error);
    }
}
