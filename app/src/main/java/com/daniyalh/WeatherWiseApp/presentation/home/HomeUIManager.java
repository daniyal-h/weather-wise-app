package com.daniyalh.WeatherWiseApp.presentation.home;

import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.presentation.UIConstants;

public class HomeUIManager {
    private final HomePage homePage;
    private AutoCompleteTextView autoCompleteCityTextView;
    private RecyclerView favouritesRecyclerView;
    private Button clearFavouritesButton;
    boolean isSelecting = false;

    public HomeUIManager(HomePage homePage) {
        this.homePage = homePage;
        initializeUI();
        setListeners();
    }

    private void initializeUI() {
        autoCompleteCityTextView = homePage.findViewById(R.id.autocomplete_city_text_view);
        autoCompleteCityTextView.setThreshold(1);
        autoCompleteCityTextView.setAdapter(homePage.getCityCursorAdapter());

        favouritesRecyclerView = homePage.findViewById(R.id.favourites_recycler_view);
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(homePage));

        clearFavouritesButton = homePage.findViewById(R.id.back_home_button);
    }

    public void setFavouritesAdapter(FavouritesAdapter favouritesAdapter) {
        favouritesRecyclerView.setAdapter(favouritesAdapter);
    }

    public void minimizeAutocomplete() {
        autoCompleteCityTextView.setText("");
        autoCompleteCityTextView.clearFocus();
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
                    homePage.debounceSearch(s.toString().trim());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        autoCompleteCityTextView.setOnItemClickListener((parent, view, position, id) -> {
            isSelecting = true; // prevents TextWatcher after selection
            homePage.handleCitySelection((Cursor) parent.getItemAtPosition(position));
            isSelecting = false; // reset flag after handling
        });

        clearFavouritesButton.setOnClickListener(v -> handleClearing());
    }

    private void handleClearing() {
        FavouritesAdapter favouritesAdapter = homePage.getFavouritesAdapter();

        // use an alert to double-check clearing
        if (favouritesAdapter != null && favouritesAdapter.getItemCount() > 0) {
            new AlertDialog.Builder(homePage)
                    .setTitle("Confirm")
                    .setMessage("Are you sure you want to clear all favourites?")
                    .setPositiveButton("Yes", (dialog, which) -> homePage.clearFavourites())
                    .setNegativeButton("No", null) // Dismisses the dialog
                    .show();
        }
        else showToast("No favourites to clear", Toast.LENGTH_SHORT);
    }

    public void showToast(String message, int duration) {
        Toast.makeText(homePage, message, duration).show();
    }
}