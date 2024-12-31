package com.daniyalh.WeatherWiseApp.logic;

import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;

import com.daniyalh.WeatherWiseApp.data.MyDatabaseHelper;
import com.daniyalh.WeatherWiseApp.objects.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchManager implements ISearchManager {
    private final MyDatabaseHelper myDatabase;
    private final ExecutorService executorService;
    private final Handler mainHandler;
    private Map<String, String[]> favouriteCities = new HashMap<>();

    public SearchManager(MyDatabaseHelper myDatabase) {
        this.myDatabase = myDatabase;
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }
    @Override
    public void searchCities(String query, SearchCallback callback) {
        if (query == null || query.trim().isEmpty()) {
            callback.onError("please enter a city");
            return;
        }

        // pass the cursor to the callback
        callback.onResults(myDatabase.getCitiesByQuery(query));
    }

    public void getFavourites(FavouritesCallback callback) {
        executorService.execute(() -> {
            List<String> favourites = new ArrayList<>();
            Cursor cursor = null;
            try {
                cursor = myDatabase.getFavouriteCities();
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        int cityID = cursor.getInt(cursor.getColumnIndexOrThrow("cityID"));
                        String displayName = cursor.getString(cursor.getColumnIndexOrThrow("display_name"));
                        String country = cursor.getString(cursor.getColumnIndexOrThrow("country"));

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
                    cursor.close();
                }
            }
        });
    }

    public void clearFavourites() {
        myDatabase.clearFavourites();
    }

    private void trackFavourites(String displayName, int cityID, String country) {
        String cityName = displayName.substring(0, displayName.indexOf(","));
        String countryCode = displayName.substring(displayName.indexOf(",")+2);
        favouriteCities.put(displayName, new String[]{String.valueOf(cityID), cityName, country, countryCode});
    }

    public String[] getFavouriteDetails(String displayName) {
        return favouriteCities.get(displayName);
    }

    // Call this method to clean up the executor service when no longer needed
    public void shutdown() {
        executorService.shutdown();
    }
}
