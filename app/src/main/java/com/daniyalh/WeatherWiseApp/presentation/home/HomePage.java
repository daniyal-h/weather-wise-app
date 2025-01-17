package com.daniyalh.WeatherWiseApp.presentation.home;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.data.DatabaseHelper;
import com.daniyalh.WeatherWiseApp.logic.weather.FavouritesManager;
import com.daniyalh.WeatherWiseApp.logic.weather.IFavouritesManager;
import com.daniyalh.WeatherWiseApp.logic.ISearchManager;
import com.daniyalh.WeatherWiseApp.logic.SearchManager;
import com.daniyalh.WeatherWiseApp.presentation.UIConstants;
import com.daniyalh.WeatherWiseApp.presentation.weather.WeatherPage;

import java.util.List;

public class HomePage extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private SearchManager searchManager;
    private FavouritesManager favouritesManager;
    private SearchHelper searchHelper;
    private FavouritesCoordinator favouritesCoordinator;
    private HomeUIManager homeUIManager;

    private AutoCompleteTextView autoCompleteCityTextView;
    private CityCursorAdapter cityCursorAdapter;
    private RecyclerView favouritesRecyclerView;
    private FavouritesAdapter favouritesAdapter;
    private Button clearFavouritesButton;
    private final Handler queryHandler = new Handler();
    private Runnable queryRunnable;
    private static final int DEBOUNCE_DELAY = 300; // Delay in ms
    private boolean isSelecting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeDatabase();
        initializeClasses();
        //initializeUI();
        //setListeners();
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

    private void initializeClasses() {
        searchHelper = new SearchHelper(this);
        favouritesCoordinator = new FavouritesCoordinator(this);
        homeUIManager = new HomeUIManager(this);

        searchManager = new SearchManager(dbHelper);
        favouritesManager = FavouritesManager.getInstance();
        favouritesManager.injectDatabase(dbHelper);
    }

    // bridging methods to connect helpers of Home Page
    // TODO ORDER BRIDGER METHODS BETTER

    public void debounceSearch(String query) {
        searchHelper.debounceSearch(query);
    }

    public CityCursorAdapter getCityCursorAdapter() {
        return searchHelper.getCityCursorAdapter();
    }

    public FavouritesAdapter getFavouritesAdapter() {
        return favouritesCoordinator.getFavouritesAdapter();
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

    public void handleCitySelection(Cursor cursor) {
        searchHelper.handleCitySelection(cursor);
    }

    public void clearFavourites() {
        favouritesCoordinator.clearFavourites();
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










    private void initializeUI() {
        cityCursorAdapter = new CityCursorAdapter(this, null);
        autoCompleteCityTextView = findViewById(R.id.autocomplete_city_text_view);
        autoCompleteCityTextView.setThreshold(1);
        autoCompleteCityTextView.setAdapter(cityCursorAdapter);

        favouritesRecyclerView = findViewById(R.id.favourites_recycler_view);
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        clearFavouritesButton = findViewById(R.id.back_home_button);
    }

    private void displayFavourites() {
        /*
        Display the favourites within the recycler view when called
        The query should propagated through the logic layer to data
        Handle clicking of a favourite to forecast weather
         */
        favouritesManager.getFavourites(new IFavouritesManager.FavouritesCallback() {
            @Override
            public void onFavouritesFetched(List<String> favourites) {
                favouritesAdapter = new FavouritesAdapter(HomePage.this, favourites, displayName -> {
                    String[] favouriteDetails = favouritesManager.getFavouriteDetails(displayName);
                    int cityID = Integer.parseInt(favouriteDetails[0]);
                    String cityName = favouriteDetails[1];
                    String countryCode = favouriteDetails[2];
                    int isFavourite = 1; // always a favourite

                    // forecast the entry
                    forecastDetails(cityID, cityName, countryCode, isFavourite);
                });
                favouritesRecyclerView.setAdapter(favouritesAdapter);
            }

            @Override
            public void onError(Exception error) {
                Log.e("MainActivity", "Error fetching favourites", error);
                showToast("Error fetching favourites", Toast.LENGTH_SHORT);
            }
        });
    }

    private void setListeners() {
        autoCompleteCityTextView.addTextChangedListener(new TextWatcher() {
            // update the drop down as user types in letters
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // update when useful change is detected
                if (!isSelecting && !s.toString().trim().equals("")
                        && !s.toString().equals(UIConstants.SELECTION_FLAG)) {
                    debounceSearchOG(s.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        autoCompleteCityTextView.setOnItemClickListener((parent, view, position, id) -> {
            isSelecting = true; // prevents TextWatcher after selection
            handleCitySelectionOG((Cursor) parent.getItemAtPosition(position));
            isSelecting = false; // reset flag after handling
        });

        clearFavouritesButton.setOnClickListener(v -> handleClearing());
    }

    private void debounceSearchOG(String query) {
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
                    showToast("No results found", Toast.LENGTH_SHORT);

                Cursor oldCursor = cityCursorAdapter.swapCursor(cursor); // update drop down
                if (oldCursor != null) {
                    oldCursor.close(); // close the old cursor
                }
            }
            @Override
            public void onError(String error) {
                showToast(error, Toast.LENGTH_SHORT);
            }
        });
    }

    private void handleCitySelectionOG(Cursor cursor) {
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
        autoCompleteCityTextView.setText("");
        autoCompleteCityTextView.clearFocus();

        forecastDetails(cityID, cityName, countryCode, isFavourite);
    }

    private void handleClearing() {
        // use an alert to double-check clearing


        if (favouritesAdapter != null && favouritesAdapter.getItemCount() > 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm")
                    .setMessage("Are you sure you want to clear all favourites?")
                    .setPositiveButton("Yes", (dialog, which) -> clearFavouritesOG())
                    .setNegativeButton("No", null) // Dismisses the dialog
                    .show();
        }
        else showToast("No favourites to clear", Toast.LENGTH_SHORT);
    }

    private void clearFavouritesOG() {
        /*
        Clear the favourites and update the favourites display (should be empty)
         */
        favouritesManager.clearFavourites(new IFavouritesManager.ClearFavouritesCallback() {
            @Override
            public void onClearSuccess() {
                displayFavourites();
                showToast("Favourites cleared successfully", Toast.LENGTH_SHORT);
            }

            @Override
            public void onClearFailure(Exception error) {
                Log.e("MainActivity", "Error fetching favourites", error);
                showToast("Error fetching favourites", Toast.LENGTH_SHORT);
            }
        });
    }

    private void cleanup() {
        favouritesManager.shutdown();

        if (cityCursorAdapter != null)
            cityCursorAdapter.changeCursor(null); // close the cursor when done

        if (dbHelper != null)
            dbHelper.close(); // close DB when done
    }

    @Override
    protected void onDestroy() {
        cleanup();
        super.onDestroy();
    }
}