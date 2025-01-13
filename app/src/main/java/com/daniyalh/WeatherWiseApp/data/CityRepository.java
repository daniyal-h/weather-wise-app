package com.daniyalh.WeatherWiseApp.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CityRepository {
    private final DatabaseHelper dbHelper;

    public CityRepository(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public Cursor getFavouriteCities() {
        // return a cursor with all favourite cities
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT cityID, name || ', ' || country_code AS display_name, country "
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
        // return a cursor with top 10 cities through a following wild card search
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "SELECT cityID AS _id, country_code, name || ', ' || country AS display_name, is_favourite"
                +  " FROM CITIES WHERE name LIKE ? LIMIT 10";
        return database.rawQuery(sql, new String[]{query + "%"});
    }

    public void clearFavourites() {
        // clear all favourite cities by setting them to 0
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "UPDATE cities SET is_favourite = 0 WHERE is_favourite = 1";
        db.execSQL(sql, new Object[]{});
    }
}
