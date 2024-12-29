package com.daniyalh.WeatherWiseApp.presentation;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.data.MyDatabaseHelper;
import com.daniyalh.WeatherWiseApp.logic.CityManager;
import com.daniyalh.WeatherWiseApp.logic.ISearchManager;
import com.daniyalh.WeatherWiseApp.logic.SearchManager;
import com.daniyalh.WeatherWiseApp.logic.WeatherManager;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper myDatabase;
    private WeatherManager weatherManager;
    private CityManager cityManager;
    private UIManager uiManager;
    private SearchManager searchManager;
    private AutoCompleteTextView autoCompleteCityTextView;
    private CityCursorAdapter cityCursorAdapter;
    private View RootView;
    boolean isSelecting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        initializeDatabase();
        initializeLogicClasses();
        initializeUIManager();
        initializeWeatherController();
        initializeUI();
        setListeners();
    }

    private void initializeDatabase() {
        myDatabase = MyDatabaseHelper.getInstance(this);
    }

    private void initializeLogicClasses() {
        //cityManager = new CityManager();
        //weatherManager = new WeatherManager(this);
        searchManager = new SearchManager(myDatabase);
    }

    private void initializeUIManager() {
        View rootView = findViewById(R.id.root_layout);
        uiManager = new UIManager(this, rootView, searchManager);
    }

    private void initializeWeatherController() {
        //weatherController = new WeatherController(weatherManager, cityManager, uiManager);
        //uiManager.setWeatherController(weatherController);
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
                if(!isSelecting && !s.toString().equals("inSelection")) {
                    searchManager.searchCities(s.toString(), new ISearchManager.SearchCallback() {
                        @Override
                        public void onResults(Cursor cursor) {
                            cityCursorAdapter.changeCursor(cursor);
                        }

                        @Override
                        public void onError(String error) {
                            uiManager.showToast(error, Toast.LENGTH_SHORT);
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
        String pair = cursor.getString(cursor.getColumnIndexOrThrow("display_name"));
        String cityName = pair.substring(0, pair.indexOf(","));
        String countryName = pair.substring(pair.indexOf(",")+2); // Winnipeg, *Canada* (,+2)
        String countryCode = cursor.getString(cursor.getColumnIndexOrThrow("country_code"));

        autoCompleteCityTextView.setText("");
        autoCompleteCityTextView.clearFocus();

        //weatherController.fetchWeather(cityName, countryName, country_code);
        Intent intent = new Intent(MainActivity.this, ForecastDetailActivity.class);
        intent.putExtra(Constants.EXTRA_CITY_NAME, cityName);
        intent.putExtra(Constants.EXTRA_COUNTRY_NAME, countryName);
        intent.putExtra(Constants.EXTRA_COUNTRY_CODE, countryCode);

        // start forecastDetailActivity for selected city
        startActivity(intent);

        uiManager.hideKeyboard(autoCompleteCityTextView);
    }

    public void cleanup() {
        cityCursorAdapter.changeCursor(null); // Close the cursor when done
    }

    @Override
    protected void onDestroy() {
        uiManager.cleanup();
        cleanup();
        super.onDestroy();
    }
}