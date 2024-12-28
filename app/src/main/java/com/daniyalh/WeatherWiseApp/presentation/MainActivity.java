package com.daniyalh.WeatherWiseApp.presentation;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.data.MyDatabaseHelper;
import com.daniyalh.WeatherWiseApp.logic.CityManager;
import com.daniyalh.WeatherWiseApp.logic.SearchManager;
import com.daniyalh.WeatherWiseApp.logic.WeatherManager;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper myDatabase;
    private WeatherManager weatherManager;
    private CityManager cityManager;
    private UIManager uiManager;
    private WeatherController weatherController;
    private SearchManager searchManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        initializeDatabase();
        initializeLogicClasses();
        initializeUIManager();
        initializeWeatherController();
        //setEventListeners();
    }

    private void initializeDatabase() {
        myDatabase = MyDatabaseHelper.getInstance(this);
    }

    private void initializeLogicClasses() {
        cityManager = new CityManager();
        weatherManager = new WeatherManager(this);
        searchManager = new SearchManager(myDatabase);
    }

    private void initializeUIManager() {
        View rootView = findViewById(R.id.root_layout);
        uiManager = new UIManager(this, rootView, searchManager);
    }

    private void initializeWeatherController() {
        weatherController = new WeatherController(weatherManager, cityManager, uiManager);
        uiManager.setWeatherController(weatherController);
    }
    /*
    private void setEventListeners() {
        // run if button is clicked
        uiManager.getGetWeatherButton().setOnClickListener(this::handleWeatherRequest);

        // run if enter key is pressed
        uiManager.getCityInputEditText().setOnEditorActionListener((v, actionId, event) -> {
            Log.d("MainActivity", "Editor action ID: " + actionId);
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
                handleWeatherRequest(v);
                return true; // Indicates that the action has been handled
            }
            return false;
        });

        // secondary enter key check (fallback)
        uiManager.getCityInputEditText().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                handleWeatherRequest(v);
                return true; // Indicates that the key event has been handled
            }
            return false;
        });

        uiManager.getCloseAppButton().setOnClickListener(view -> finish());
    }

    private void handleWeatherRequest(View v) {
        uiManager.hideKeyboard(v);

        // Get City Name Input
        //String cityName = uiManager.getCityName();
        //if (cityName.isEmpty()) {
            uiManager.showToast("Please enter a city name", Toast.LENGTH_SHORT);
            return;
        }

        // Fetch Weather Data
        //weatherController.fetchWeather(cityName);
    //}
    8/
     */

    @Override
    protected void onDestroy() {
        uiManager.cleanup();
        super.onDestroy();
    }
}