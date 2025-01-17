package com.daniyalh.WeatherWiseApp.presentation.home;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.widget.Toast;

import com.daniyalh.WeatherWiseApp.data.DatabaseHelper;
import com.daniyalh.WeatherWiseApp.logic.ISearchManager;
import com.daniyalh.WeatherWiseApp.logic.SearchManager;
import com.daniyalh.WeatherWiseApp.objects.City;
import com.daniyalh.WeatherWiseApp.presentation.UIConstants;
import com.daniyalh.WeatherWiseApp.presentation.weather.WeatherPage;

public class SearchHelper {
    private final HomePage homePage;
    private final SearchManager searchManager;
    private final DatabaseHelper databaseHelper;
    private final CityCursorAdapter cityCursorAdapter;
    private final Handler queryHandler = new Handler();
    private Runnable queryRunnable;
    private static final int DEBOUNCE_DELAY = 300; // Delay in ms
    public SearchHelper(HomePage homePage) {
        this.homePage = homePage;
        databaseHelper = DatabaseHelper.getInstance();
        searchManager = new SearchManager(databaseHelper);
        cityCursorAdapter = new CityCursorAdapter(homePage, null);
    }

    public CityCursorAdapter getCityCursorAdapter() {
        return cityCursorAdapter;
    }

    public void debounceSearch(String query) {
        // Cancel any previously scheduled tasks
        if (queryRunnable != null) {
            queryHandler.removeCallbacks(queryRunnable);
        }

        // Schedule a new query
        queryRunnable = () -> handleSearching(query);
        queryHandler.postDelayed(queryRunnable, DEBOUNCE_DELAY);
    }

    private void handleSearching(String query) {
        // update results based on the callback
        searchManager.searchCities(query, new ISearchManager.SearchCallback() {
            @Override
            public void onResults(Cursor cursor) {
                if (cursor == null || cursor.getCount() == 0)
                    homePage.showToast("No results found", Toast.LENGTH_SHORT);

                Cursor oldCursor = cityCursorAdapter.swapCursor(cursor); // update drop down
                if (oldCursor != null) {
                    oldCursor.close(); // close the old cursor
                }
            }
            @Override
            public void onError(String error) {
                homePage.showToast(error, Toast.LENGTH_SHORT);
            }
        });
    }

    public void handleCitySelection(Cursor cursor) {
        // set variables
        int cityID = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String pair = cursor.getString(cursor.getColumnIndexOrThrow("display_name"));
        String cityName = pair.substring(0, pair.indexOf(","));
        String countryCode = pair.substring(pair.indexOf(",")+2); // Winnipeg, *CA* (comma+2)
        int isFavourite = cursor.getInt(cursor.getColumnIndexOrThrow("is_favourite")); // 0 or 1

        // close the old cursor so it doesn't remain active while viewing forecast
        Cursor oldCursor = cityCursorAdapter.swapCursor(null);
        if (oldCursor != null) {
            oldCursor.close();
        }

        homePage.minimizeAutocomplete();
        homePage.forecastDetails(cityID, cityName, countryCode, isFavourite);
    }
}
