package com.daniyalh.WeatherWiseApp.data.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.daniyalh.WeatherWiseApp.data.DatabaseHelper;
import com.daniyalh.WeatherWiseApp.data.callbacks.IForecastCacheUpdateCallback;
import com.daniyalh.WeatherWiseApp.logic.forecast.ForecastManager;
import com.daniyalh.WeatherWiseApp.logic.forecast.IForecastManager;
import com.daniyalh.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;
import com.daniyalh.WeatherWiseApp.objects.City;
import com.daniyalh.WeatherWiseApp.objects.Forecast;

public class ForecastRepository {
    private final DatabaseHelper dbHelper;
    private ForecastManager forecastManager;

    private static final int OUTDATED_LIMIT = 60 * 60 * 1000; // one hour in milliseconds

    public ForecastRepository(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void setForecastManager(ForecastManager forecastManager) {
        this.forecastManager = forecastManager;
    }

    public void getForecastDetails(City city, IForecastManager.IForecastDetailsCallback callback) {
        /*
        Asynchronously acquire forecast details for given city
        Forecast Table caches forecast JSONs
        If the forecast stored is updated (<= 1 hour), return that
        Otherwise, update the cache and return the new one
        */

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "SELECT last_updated, forecastJSON FROM Forecast" +
                    " WHERE cityID = ? AND last_updated != -1";

        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(city.getCityID())});
        long currentTime = System.currentTimeMillis();

        // check if the result is empty
        if (cursor.moveToFirst()) {
            // check if the result is viable (<= 1 hour)
            long lastUpdated = cursor.getLong(cursor.getColumnIndexOrThrow("last_updated"));

            if (currentTime - lastUpdated <= OUTDATED_LIMIT) {
                // get all forecasts from the cache
                Forecast[] forecastDetails = extractForecastDetailsFromCursor(
                        cursor.getString(cursor.getColumnIndexOrThrow("forecastJSON")));

                city.setForecastLastUpdate(lastUpdated); // store last update time

                cursor.close();

                callback.onSuccess(forecastDetails); // return cached forecast objects
                return;
            }
        }
        cursor.close();

        // cache is outdated or empty; fetch new data
        cacheForecast(city, new IForecastCacheUpdateCallback() {
            @Override
            public void onCacheUpdated(String forecastJSON) {
                callback.onSuccess(extractForecastDetailsFromCursor(forecastJSON));
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    private Forecast[] extractForecastDetailsFromCursor(String forecastJSON) {
        return forecastManager.parseJSON(forecastJSON);
    }

    public void cacheForecast(City city, IForecastCacheUpdateCallback callback) {
        /*
        Update Forecast Table with the new forecast JSON
        Fetch JSON from ForecastManager and insert or update
        Should be done asynchronously
         */

        forecastManager.getForecastJSON(city, new IForecastManager.IForecastCallback() {
            @Override
            public void onSuccess(String forecastJSON) {
                try {
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();

                    values.put("cityID", city.getCityID());
                    values.put("last_updated", System.currentTimeMillis());
                    values.put("forecastJSON", forecastJSON);

                    city.setForecastLastUpdate(values.getAsLong("last_updated"));

                    // insert or update existing cache (outdated)
                    database.insertWithOnConflict("Forecast", null, values, SQLiteDatabase.CONFLICT_REPLACE);

                    // notify success
                    callback.onCacheUpdated(forecastJSON);
                }
                catch (InvalidJsonParsingException e) {
                    callback.onError(e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }
}
