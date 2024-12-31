package com.daniyalh.WeatherWiseApp.logic;

import android.database.Cursor;

import java.util.List;

public interface ISearchManager {
    void searchCities(String query, SearchCallback callback);

    void getFavourites(FavouritesCallback callback);

    interface SearchCallback {
        void onResults(Cursor cursor);
        void onError(String error);
    }

    interface FavouritesCallback {
        void onFavouritesFetched(List<String> favourites);
        void onError(Exception error);
    }
}
