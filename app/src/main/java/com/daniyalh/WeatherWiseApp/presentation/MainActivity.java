package com.daniyalh.WeatherWiseApp.presentation;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.data.MyDatabaseHelper;
import com.daniyalh.WeatherWiseApp.logic.ISearchManager;
import com.daniyalh.WeatherWiseApp.logic.SearchManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper myDatabase;
    private SearchManager searchManager;
    private AutoCompleteTextView autoCompleteCityTextView;
    private CityCursorAdapter cityCursorAdapter;
    private RecyclerView favouritesRecyclerView;
    private FavouritesAdapter favouritesAdapter;
    boolean isSelecting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeDatabase();
        initializeLogicClasses();
        initializeUI();
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayFavourites();
    }

    private void initializeDatabase() {
        myDatabase = MyDatabaseHelper.getInstance(this);
    }

    private void initializeLogicClasses() {
        searchManager = new SearchManager(myDatabase);
    }

    private void initializeUI() {
        cityCursorAdapter = new CityCursorAdapter(this, null);
        autoCompleteCityTextView = findViewById(R.id.autocomplete_city_text_view);
        autoCompleteCityTextView.setThreshold(1);
        autoCompleteCityTextView.setAdapter(cityCursorAdapter);

        favouritesRecyclerView = findViewById(R.id.favourites_recycler_view);
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayFavourites();

    }

    private void displayFavourites() {
        searchManager.getFavourites(new ISearchManager.FavouritesCallback() {
            @Override
            public void onFavouritesFetched(List<String> favourites) {
                Log.d("MainActivity", "Fetched Favourites: " + favourites);
                favouritesAdapter = new FavouritesAdapter(MainActivity.this, favourites, city -> {
                    // TODO
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
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isSelecting && !s.toString().equals(UIConstants.SELECTION_FLAG)) {
                    searchManager.searchCities(s.toString(), new ISearchManager.SearchCallback() {
                        @Override
                        public void onResults(Cursor cursor) {
                            if (cursor == null || cursor.getCount() == 0)
                                showToast("No results found", Toast.LENGTH_SHORT);
                            cityCursorAdapter.swapCursor(cursor); // update drop down
                        }

                        @Override
                        public void onError(String error) {
                            showToast(error, Toast.LENGTH_SHORT);
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        autoCompleteCityTextView.setOnItemClickListener((parent, view, position, id) -> {
            isSelecting = true; // prevents TextWatcher after selection
            handleCitySelection((Cursor) parent.getItemAtPosition(position));
            isSelecting = false; // reset flag after handling
        });
    }

    private void handleCitySelection(Cursor cursor) {
        int cityID = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String pair = cursor.getString(cursor.getColumnIndexOrThrow("display_name"));
        String cityName = pair.substring(0, pair.indexOf(","));
        String countryName = pair.substring(pair.indexOf(",")+2); // Winnipeg, *Canada* (,+2)
        String countryCode = cursor.getString(cursor.getColumnIndexOrThrow("country_code"));
        int isFavourite = cursor.getInt(cursor.getColumnIndexOrThrow("is_favourite")); // 0 or 1

        autoCompleteCityTextView.setText("");
        autoCompleteCityTextView.clearFocus();

        Intent intent = new Intent(MainActivity.this, ForecastDetailActivity.class);
        intent.putExtra(UIConstants.EXTRA_CITY_ID, cityID);
        intent.putExtra(UIConstants.EXTRA_CITY_NAME, cityName);
        intent.putExtra(UIConstants.EXTRA_COUNTRY_NAME, countryName);
        intent.putExtra(UIConstants.EXTRA_COUNTRY_CODE, countryCode);
        intent.putExtra(UIConstants.EXTRA_IS_FAVOURITE, isFavourite);

        // start forecastDetailActivity for selected city
        startActivity(intent);

        hideKeyboard(autoCompleteCityTextView);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showToast(String message, int duration) {
        Toast.makeText(this, message, duration).show();
    }

    private void cleanup() {
        searchManager.shutdown();

        if (cityCursorAdapter != null)
            cityCursorAdapter.changeCursor(null); // Close the cursor when done

        if (myDatabase != null)
            myDatabase.close();
    }

    @Override
    protected void onDestroy() {
        cleanup();
        super.onDestroy();
    }
}