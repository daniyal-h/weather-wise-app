package com.daniyalh.WeatherWiseApp.logic.weather;

import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;

import com.daniyalh.WeatherWiseApp.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavouritesManager implements IFavouritesManager {
    private DatabaseHelper dhHelper;
    private ExecutorService executorService;
    private Handler mainHandler;
    private final Map<String, String[]> favouriteCities = new HashMap<>();
    private static FavouritesManager instance;

    private FavouritesManager() {
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public static FavouritesManager getInstance() {
        if (instance == null) {
            instance = new FavouritesManager();
        }
        return instance;
    }

    @Override
    public void injectDatabase(DatabaseHelper dhHelper) {
        this.dhHelper = dhHelper;
    }

    @Override
    public void setAsynchronicity(ExecutorService executorService, Handler mainHandler) {
        this.executorService = executorService;
        this.mainHandler = mainHandler;
    }

    @Override
    public void toggleFavourite(int cityID, boolean isFavourite) {
        // favourite or unfavourite a given city
        dhHelper.getCityRepository().updateFavouriteStatus(cityID, isFavourite);
    }

    @Override
    public void getFavourites(IFavouritesManager.FavouritesCallback callback) {
        /*
        Get favourites asynchronously through a callback thread
        Returns a List of city names with their country code
        Also keep track of all favourites' ID and country name
         */
        executorService.execute(() -> {
            List<String> favourites = new ArrayList<>();
            Cursor cursor = null;

            try {
                cursor = dhHelper.getCityRepository().getFavouriteCities();
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        // get all necessary parameters
                        int cityID = cursor.getInt(cursor.getColumnIndexOrThrow("cityID"));
                        String displayName = cursor.getString(cursor.getColumnIndexOrThrow("display_name"));

                        // track favourites details and prepare list with display names
                        trackFavourites(displayName, cityID);
                        favourites.add(displayName);
                    } while (cursor.moveToNext());
                }

                // Post the result back to the main thread
                mainHandler.post(() -> callback.onFavouritesFetched(favourites));
            } catch (Exception e) {
                // Post the error back to the main thread
                mainHandler.post(() -> callback.onError(e));
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close(); // clean up
                }
            }
        });
    }

    @Override
    public void clearFavourites(IFavouritesManager.ClearFavouritesCallback callback) {
        /*
        Clear all favourites asynchronously through a callback thread
         */
        executorService.execute(() -> {
            try {
                dhHelper.getCityRepository().clearFavourites();
                mainHandler.post(callback::onClearSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onClearFailure(e));
            }
        });
    }

    private void trackFavourites(String displayName, int cityID) {
        // track each favourite in a Map
        String cityName = displayName.substring(0, displayName.indexOf(","));
        String countryCode = displayName.substring(displayName.indexOf(",")+2);

        favouriteCities.put(displayName,
                new String[]{String.valueOf(cityID), cityName, countryCode});
    }

    @Override
    public String[] getFavouriteDetails(String displayName) {
        return favouriteCities.get(displayName);
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }
}