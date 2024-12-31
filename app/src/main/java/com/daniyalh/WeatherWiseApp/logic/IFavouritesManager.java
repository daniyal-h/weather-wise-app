package com.daniyalh.WeatherWiseApp.logic;

import java.util.List;

public interface IFavouritesManager {
    void getFavourites(IFavouritesManager.FavouritesCallback callback);
    void clearFavourites(IFavouritesManager.ClearFavouritesCallback callback);
    String[] getFavouriteDetails(String displayName);
    void shutdown();

    interface FavouritesCallback {
        void onFavouritesFetched(List<String> favourites);
        void onError(Exception error);
    }

    interface ClearFavouritesCallback {
        void onClearSuccess();
        void onClearFailure(Exception error);
    }
}
