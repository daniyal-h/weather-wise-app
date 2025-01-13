package com.daniyalh.WeatherWiseApp.logic.weather;

import android.os.Handler;

import com.daniyalh.WeatherWiseApp.data.DatabaseHelper;

import java.util.List;
import java.util.concurrent.ExecutorService;

public interface IFavouritesManager {
    public void injectDatabase(DatabaseHelper dhHelper);
    void setAsynchronicity(ExecutorService executorService, Handler mainHandler);
    void toggleFavourite(int cityID, boolean isFavourite);
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
