package com.daniyalh.WeatherWiseApp.presentation.home;

import android.util.Log;
import android.widget.Toast;

import com.daniyalh.WeatherWiseApp.data.DatabaseHelper;
import com.daniyalh.WeatherWiseApp.logic.weather.FavouritesManager;
import com.daniyalh.WeatherWiseApp.logic.weather.IFavouritesManager;

import java.util.List;

public class FavouritesCoordinator {
    private final HomePage homePage;
    private final FavouritesManager favouritesManager;
    private FavouritesAdapter favouritesAdapter;

    public FavouritesCoordinator(HomePage homePage) {
        this.homePage = homePage;
        favouritesManager = FavouritesManager.getInstance();
        favouritesManager.injectDatabase(DatabaseHelper.getInstance());
    }

    public FavouritesAdapter getFavouritesAdapter() {
        return favouritesAdapter;
    }

    public void displayFavourites() {
        /*
        Display the favourites within the recycler view when called
        The query should propagated through the logic layer to data
        Handle clicking of a favourite to forecast weather
         */
        favouritesManager.getFavourites(new IFavouritesManager.FavouritesCallback() {
            @Override
            public void onFavouritesFetched(List<String> favourites) {
                favouritesAdapter = new FavouritesAdapter(homePage, favourites, displayName -> {
                    String[] favouriteDetails = favouritesManager.getFavouriteDetails(displayName);
                    int cityID = Integer.parseInt(favouriteDetails[0]);
                    String cityName = favouriteDetails[1];
                    String countryCode = favouriteDetails[2];
                    int isFavourite = 1; // always a favourite

                    // forecast the entry
                    homePage.forecastDetails(cityID, cityName, countryCode, isFavourite);
                });
                homePage.setFavouritesAdapter(favouritesAdapter);
            }

            @Override
            public void onError(Exception error) {
                Log.e("MainActivity", "Error fetching favourites", error);
                homePage.showToast("Error fetching favourites", Toast.LENGTH_SHORT);
            }
        });
    }

    public void clearFavourites() {
        /*
        Clear the favourites and update the favourites display (should be empty)
         */
        favouritesManager.clearFavourites(new IFavouritesManager.ClearFavouritesCallback() {
            @Override
            public void onClearSuccess() {
                displayFavourites();
                homePage.showToast("Favourites cleared successfully", Toast.LENGTH_SHORT);
            }

            @Override
            public void onClearFailure(Exception error) {
                homePage.showToast("Error fetching favourites", Toast.LENGTH_SHORT);
            }
        });
    }

    public void cleanUp() {
        favouritesManager.shutdown();
    }
}
