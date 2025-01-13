package com.daniyalh.WeatherWiseApp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.daniyalh.WeatherWiseApp.logic.IWeatherManager;
import com.daniyalh.WeatherWiseApp.logic.WeatherManager;
import com.daniyalh.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;
import com.daniyalh.WeatherWiseApp.objects.City;

public class WeatherRepository {
    private final DatabaseHelper dbHelper;
    private WeatherManager weatherManager;

    private static final int OUTDATED_LIMIT = 10 * 60 * 1000; // ten minutes in milliseconds

    public WeatherRepository(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void setWeatherManager(WeatherManager weatherManager) {
        this.weatherManager = weatherManager;
    }

    public void getWeatherDetails(City city, IWeatherManager.IWeatherDetailsCallback callback) {
        /*
        Asynchronously acquire weather details for given city
        Weather Table stores cached immediate weather
        If the forecast stored is updated (<= 10 minutes), return that
        Otherwise, update the value and return the new one
         */
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "SELECT last_updated, temp, feels_like, description, humidity, wind_speed, offset, sunrise, sunset, tod" +
                " FROM Weather WHERE cityID = ? AND last_updated != -1";

        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(city.getCityID())});
        long currentTime = System.currentTimeMillis();

        // check if the result is empty
        if (cursor.moveToFirst()) {
            // check if the result is viable (<= 10 minutes)
            long lastUpdated = cursor.getLong(cursor.getColumnIndexOrThrow("last_updated"));

            if (currentTime - lastUpdated <= OUTDATED_LIMIT) {
                String[] weatherDetails = extractWeatherDetailsFromCursor(cursor); // return cached forecast
                cursor.close();
                callback.onSuccess(weatherDetails);
                return;
            }
        }
        cursor.close();

        // cache is outdated or empty; fetch new data
        cacheWeather(city, new ICacheUpdateCallback() {
            @Override
            public void onCacheUpdated(String[] newWeatherData) {
                callback.onSuccess(newWeatherData);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    private String[] extractWeatherDetailsFromCursor(Cursor cursor) {
        // convert the cursor to an array of strings for the forecast
        return new String[] {
                cursor.getString(cursor.getColumnIndexOrThrow("last_updated")),
                cursor.getString(cursor.getColumnIndexOrThrow("temp")),
                cursor.getString(cursor.getColumnIndexOrThrow("feels_like")),
                cursor.getString(cursor.getColumnIndexOrThrow("description")),
                cursor.getString(cursor.getColumnIndexOrThrow("humidity")),
                cursor.getString(cursor.getColumnIndexOrThrow("wind_speed")),
                cursor.getString(cursor.getColumnIndexOrThrow("offset")),
                cursor.getString(cursor.getColumnIndexOrThrow("sunrise")),
                cursor.getString(cursor.getColumnIndexOrThrow("sunset")),
                cursor.getString(cursor.getColumnIndexOrThrow("tod"))
        };
    }

    private void cacheWeather(City city, ICacheUpdateCallback callback) {
        /*
        Update Weather Table with the new weather forecast
        Fetch weather from WeatherManager and insert or update
        Should be done asynchronously
         */
        weatherManager.getWeatherJSON(city, new IWeatherManager.IWeatherCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    String[] newWeatherData = weatherManager.fetchImmediateWeather(response);

                    // Update the Weather table with the new data
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("cityID", city.getCityID());
                    values.put("last_updated", System.currentTimeMillis());
                    values.put("temp", newWeatherData[1]);
                    values.put("feels_like", newWeatherData[2]);
                    values.put("description", newWeatherData[3]);
                    values.put("humidity", newWeatherData[4]);
                    values.put("wind_speed", newWeatherData[5]);
                    values.put("offset", newWeatherData[6]);
                    values.put("sunrise", newWeatherData[7]);
                    values.put("sunset", newWeatherData[8]);
                    values.put("tod", newWeatherData[9]);

                    newWeatherData[0] = values.getAsString("last_updated");

                    // insert or update existing cache (outdated)
                    database.insertWithOnConflict("Weather", null, values, SQLiteDatabase.CONFLICT_REPLACE);

                    // Notify success
                    callback.onCacheUpdated(newWeatherData);
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
