package com.daniyalh.WeatherWiseApp.presentation;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.logic.ISearchManager;
import com.daniyalh.WeatherWiseApp.logic.SearchManager;
import com.daniyalh.WeatherWiseApp.objects.City;

public class UIManager {
    private final Context context;
    private final View rootView;
    private final SearchManager searchManager;
    private WeatherController weatherController;

    // UI Components
    private AutoCompleteTextView autoCompleteCityTextView;
    private final CityCursorAdapter cityCursorAdapter;

    private TextView cityTextView, descriptionTextView,
            sunriseLabelTextView, sunsetLabelTextView, windLabelTextView, humidityLabelTextView,
            tempTextView, feelsLikeTextView, sunriseTextView, sunsetTextView, windTextView, humidityTextView;

    private LottieAnimationView loadingIconLottie, sunIconLottie, moonIconLottie, windIconLottie, humidityIconLottie;

    private Button closeAppButton;

    private boolean isSelecting = false;

    public UIManager(Context context, View rootView, SearchManager searchManager) {
        this.context = context;
        this.rootView = rootView;
        this.searchManager = searchManager;
        cityCursorAdapter = new CityCursorAdapter(context, null);

        initializeUI();
        setupListeners();
    }

    private void initializeUI() {
        autoCompleteCityTextView = rootView.findViewById(R.id.autocomplete_city_text_view);
        autoCompleteCityTextView.setThreshold(1);
        autoCompleteCityTextView.setAdapter(cityCursorAdapter);

        cityTextView = rootView.findViewById(R.id.city_text_view);
        descriptionTextView = rootView.findViewById(R.id.description_text_view);

        sunriseLabelTextView = rootView.findViewById(R.id.sunrise_label_text_view);
        sunsetLabelTextView = rootView.findViewById(R.id.sunset_label_text_view);
        windLabelTextView = rootView.findViewById(R.id.wind_label_text_view);
        humidityLabelTextView = rootView.findViewById(R.id.humidity_label_text_view);

        closeAppButton = rootView.findViewById(R.id.close_app_button);

        tempTextView = rootView.findViewById(R.id.temperature_text_view);
        feelsLikeTextView = rootView.findViewById(R.id.feels_like_text_view);
        sunriseTextView = rootView.findViewById(R.id.sunrise_text_view);
        sunsetTextView = rootView.findViewById(R.id.sunset_text_view);
        windTextView = rootView.findViewById(R.id.wind_text_view);
        humidityTextView = rootView.findViewById(R.id.humidity_text_view);

        loadingIconLottie = rootView.findViewById(R.id.loading_icon_lottie);
        sunIconLottie = rootView.findViewById(R.id.sun_icon_lottie);
        moonIconLottie = rootView.findViewById(R.id.moon_icon_lottie);
        windIconLottie = rootView.findViewById(R.id.wind_icon_lottie);
        humidityIconLottie = rootView.findViewById(R.id.humidity_icon_lottie);
    }

    private void setupListeners() {
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
        String pair = cursor.getString(cursor.getColumnIndexOrThrow("display_name"));
        String cityName = pair.substring(0, pair.indexOf(","));
        String countryName = pair.substring(pair.indexOf(",")+2);
        String country_code = cursor.getString(cursor.getColumnIndexOrThrow("country_code"));

        autoCompleteCityTextView.setText("");
        autoCompleteCityTextView.clearFocus();

        weatherController.fetchWeather(cityName, countryName, country_code);

        hideKeyboard(autoCompleteCityTextView);
    }

    public void cleanup() {
        cityCursorAdapter.changeCursor(null); // Close the cursor when done
    }

    // Setters and Getters for UI Components
    public void setWeatherController(WeatherController weatherController) {
        this.weatherController = weatherController;
    }
    public void setCityLabel(City city) {
        cityTextView.setText(city.getCity().toUpperCase() + ", " + city.getCountryCode());
    }

    public Button getCloseAppButton() {
        return closeAppButton;
    }

    // UI Update Methods
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showLoadingIcon(boolean visible) {
        // toggle the loading icon
        if (visible) {
            loadingIconLottie.setVisibility(View.VISIBLE);
            loadingIconLottie.playAnimation();
        }
        else {
            loadingIconLottie.setVisibility(View.GONE);
            loadingIconLottie.cancelAnimation();
        }
    }

    public void setStaticUIVisibility(boolean visible) {
        // make the static UI elements invisible or visible
        int visibility = visible ? View.VISIBLE : View.INVISIBLE;

        sunriseLabelTextView.setVisibility(visibility);
        sunsetLabelTextView.setVisibility(visibility);
        windLabelTextView.setVisibility(visibility);
        humidityLabelTextView.setVisibility(visibility);

        sunIconLottie.setVisibility(visibility);
        moonIconLottie.setVisibility(visibility);
        windIconLottie.setVisibility(visibility);
        humidityIconLottie.setVisibility(visibility);

        // stop or play the animations to reduce memory usage
        if (visible) {
            sunIconLottie.playAnimation();
            moonIconLottie.playAnimation();
            windIconLottie.playAnimation();
            humidityIconLottie.playAnimation();
        }
        else { // invisible icons shouldn't be playing
            sunIconLottie.cancelAnimation();
            moonIconLottie.cancelAnimation();
            windIconLottie.cancelAnimation();
            humidityIconLottie.cancelAnimation();
        }
    }

    public void updateWeatherDetails(String[] details) {
        tempTextView.setText(details[0]);
        feelsLikeTextView.setText(details[1]);
        descriptionTextView.setText(details[2]);
        humidityTextView.setText(details[3]);
        windTextView.setText(details[4]);
        sunriseTextView.setText(details[5]);
        sunsetTextView.setText(details[6]);
        setTimeOfDay(details[7].charAt(0));
    }

    private void setTimeOfDay(char timeOfDay) {
        // set the background colour depending on the time of day
        if (timeOfDay == 'd') {
            rootView.setBackgroundColor(ContextCompat.getColor(context, R.color.day_background));
        } else { // nighttime
            rootView.setBackgroundColor(ContextCompat.getColor(context, R.color.night_background));
        }
    }

    public void resetUI() {
        setStaticUIVisibility(false);
        cityTextView.setText("");
        tempTextView.setText("");
        feelsLikeTextView.setText("");
        descriptionTextView.setText("");
        sunriseTextView.setText("");
        sunsetTextView.setText("");
        windTextView.setText("");
        humidityTextView.setText("");
    }

    public void showToast(String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    public void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}