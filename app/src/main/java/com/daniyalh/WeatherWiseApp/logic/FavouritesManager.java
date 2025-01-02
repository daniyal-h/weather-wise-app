package com.daniyalh.WeatherWiseApp.logic;

import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;

import com.daniyalh.WeatherWiseApp.data.MyDatabaseHelper;
import com.daniyalh.WeatherWiseApp.objects.City;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavouritesManager implements IFavouritesManager {
    private final MyDatabaseHelper myDatabase;
    private final ExecutorService executorService;
    private final Handler mainHandler;
    private final Map<String, String[]> favouriteCities = new HashMap<>();
    private static FavouritesManager instance;
    private FavouritesManager(MyDatabaseHelper myDatabase) {
        this.myDatabase = myDatabase;
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public static FavouritesManager getInstance(MyDatabaseHelper myDatabase) {
        if (instance == null) {
            instance = new FavouritesManager(myDatabase);
        }
        return instance;
    }

    @Override
    public void toggleFavourite(City city, boolean isFavourite) {
        // favourite or unfavourite a given city
        myDatabase.updateFavouriteStatus(city.getCityID(), isFavourite);
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
                cursor = myDatabase.getFavouriteCities();
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        // get all necessary parameters
                        int cityID = cursor.getInt(cursor.getColumnIndexOrThrow("cityID"));
                        String displayName = cursor.getString(cursor.getColumnIndexOrThrow("display_name"));
                        String country = cursor.getString(cursor.getColumnIndexOrThrow("country"));

                        // track favourites details and prepare list with display names
                        trackFavourites(displayName, cityID, country);
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
                myDatabase.clearFavourites();
                mainHandler.post(callback::onClearSuccess);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onClearFailure(e));
            }
        });
    }

    private void trackFavourites(String displayName, int cityID, String country) {
        // track each favourite in a Map
        String cityName = displayName.substring(0, displayName.indexOf(","));
        String countryCode = displayName.substring(displayName.indexOf(",")+2);

        favouriteCities.put(displayName,
                new String[]{String.valueOf(cityID), cityName, country, countryCode});
    }

    public String[] getFavouriteDetails(String displayName) {
        return favouriteCities.get(displayName);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}