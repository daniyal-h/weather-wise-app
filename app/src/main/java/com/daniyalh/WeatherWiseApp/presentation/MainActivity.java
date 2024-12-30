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

import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.data.MyDatabaseHelper;
import com.daniyalh.WeatherWiseApp.logic.ISearchManager;
import com.daniyalh.WeatherWiseApp.logic.SearchManager;
public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper myDatabase;
    private SearchManager searchManager;
    private AutoCompleteTextView autoCompleteCityTextView;
    private CityCursorAdapter cityCursorAdapter;
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

    private void initializeDatabase() {
        myDatabase = MyDatabaseHelper.getInstance(this);
    }

    private void initializeLogicClasses() {
        searchManager = new SearchManager(myDatabase);
    }

    private void initializeUI() {
        cityCursorAdapter = new CityCursorAdapter(this, null);
        View rootView = findViewById(R.id.root_layout);
        autoCompleteCityTextView = rootView.findViewById(R.id.autocomplete_city_text_view);
        autoCompleteCityTextView.setThreshold(1);
        autoCompleteCityTextView.setAdapter(cityCursorAdapter);
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

        /////////////////////////// TESTING! ///////////////
        searchManager.getFavourites(new ISearchManager.SearchCallback() {
            @Override
            public void onResults(Cursor cursor2) {
                showToast("Favourite Count: " + cursor2.getCount(), Toast.LENGTH_SHORT);
                cursor2.close();
            }

            @Override
            public void onError(String error) {

            }
        }); ///////////////////////

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