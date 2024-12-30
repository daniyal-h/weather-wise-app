package com.daniyalh.WeatherWiseApp.logic;

import android.database.Cursor;

import java.util.List;

public interface ISearchManager {
    void searchCities(String query, SearchCallback callback);

    void getFavourites(SearchCallback callback);

    interface SearchCallback {
        void onResults(Cursor cursor);
        void onError(String error);
    }
}
