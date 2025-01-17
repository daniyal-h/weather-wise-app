package com.daniyalh.WeatherWiseApp.presentation.home;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.data.DatabaseHelper;
import com.daniyalh.WeatherWiseApp.presentation.UIConstants;
import com.daniyalh.WeatherWiseApp.presentation.weather.WeatherPage;

public class HomePage extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private SearchHelper searchHelper;
    private FavouritesCoordinator favouritesCoordinator;
    private HomeUIManager homeUIManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeDatabase();
        initializeHelpers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        favouritesCoordinator.displayFavourites(); // updates favourites when returning from weather
    }

    private void initializeDatabase() {
        DatabaseHelper.initialize(this, "WeatherWiseApp.db");
        dbHelper = DatabaseHelper.getInstance();
    }

    private void initializeHelpers() {
        searchHelper = new SearchHelper(this);
        favouritesCoordinator = new FavouritesCoordinator(this);
        homeUIManager = new HomeUIManager(this);
    }

    // bridging methods to connect helpers of Home Page to one another
    public void debounceSearch(String query) {
        searchHelper.debounceSearch(query);
    }

    public CityCursorAdapter getCityCursorAdapter() {
        return searchHelper.getCityCursorAdapter();
    }

    public void handleCitySelection(Cursor cursor) {
        searchHelper.handleCitySelection(cursor);
    }

    public FavouritesAdapter getFavouritesAdapter() {
        return favouritesCoordinator.getFavouritesAdapter();
    }

    public void clearFavourites() {
        favouritesCoordinator.clearFavourites();
    }

    public void setFavouritesAdapter(FavouritesAdapter favouritesAdapter) {
        homeUIManager.setFavouritesAdapter(favouritesAdapter);
    }

    public void minimizeAutocomplete() {
        homeUIManager.minimizeAutocomplete();
    }

    public void showToast(String message, int duration) {
        homeUIManager.showToast(message, duration);
    }

    public void forecastDetails(int cityID, String cityName, String countryCode, int isFavourite) {
        Intent intent = new Intent(HomePage.this, WeatherPage.class);
        intent.putExtra(UIConstants.EXTRA_CITY_ID, cityID);
        intent.putExtra(UIConstants.EXTRA_CITY_NAME, cityName);
        intent.putExtra(UIConstants.EXTRA_COUNTRY_CODE, countryCode);
        intent.putExtra(UIConstants.EXTRA_IS_FAVOURITE, isFavourite);

        // start forecastDetailActivity for selected city
        startActivity(intent);
    }

    private void cleanup() {
        searchHelper.cleanUp();

        if (dbHelper != null)
            dbHelper.close(); // close DB when done
    }

    @Override
    protected void onDestroy() {
        cleanup();
        super.onDestroy();
    }
}