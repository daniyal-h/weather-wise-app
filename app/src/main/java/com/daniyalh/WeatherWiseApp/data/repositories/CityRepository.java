package com.daniyalh.WeatherWiseApp.data.repositories;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.daniyalh.WeatherWiseApp.data.DatabaseHelper;

public class CityRepository {
    private final DatabaseHelper dbHelper;

    public CityRepository(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public Cursor getFavouriteCities() {
        // return a cursor with all favourite cities
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT cityID, name || ', ' || country_code AS display_name "
                + "FROM cities WHERE is_favourite = 1 "
                + "ORDER BY display_name";
        return db.rawQuery(sql, null);
    }

    public void updateFavouriteStatus(int cityID, boolean isFavourite) {
        // toggle the favourite status of a given city
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "UPDATE cities SET is_favourite = ? WHERE cityID = ?";
        db.execSQL(sql, new Object[]{isFavourite ? 1 : 0, cityID});
    }

    public Cursor getCitiesByQuery(String query) {
        /*
        Mimics a wildcard search with stronger optimization
        Utilizes index of (name, population (desc))
         */
        // Get the next character for the range query
        query = query.substring(0, 1).toUpperCase() + query.substring(1);

        // Get the next character for the range query
        char lastChar = query.charAt(query.length() - 1);
        String nextQuery = query.substring(0, query.length() - 1) + (char) (lastChar + 1);

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // Construct the SQL query
        String sql = "SELECT cityID AS _id, name || ', ' || country_code AS display_name, is_favourite " +
                "FROM CITIES " +
                "WHERE name >= ? AND name < ? " +
                "ORDER BY population DESC " +
                "LIMIT 10";

        // Execute the query and return the cursor
        return database.rawQuery(sql, new String[]{query, nextQuery});
    }

    public void clearFavourites() {
        // clear all favourite cities by setting them to 0
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "UPDATE cities SET is_favourite = 0 WHERE is_favourite = 1";
        db.execSQL(sql, new Object[]{});
    }
}
