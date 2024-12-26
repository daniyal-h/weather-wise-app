package com.daniyalh.WeatherWiseApp.presentation;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.daniyalh.WeatherWiseApp.R;
import com.daniyalh.WeatherWiseApp.objects.City;

public class UIManager {
    private Context context;
    private View rootView;

    // UI Components
    private EditText cityInputEditText;
    private TextView cityTextView, descriptionTextView,
            sunriseLabelTextView, sunsetLabelTextView, windLabelTextView, humidityLabelTextView,
            tempTextView, feelsLikeTextView, sunriseTextView, sunsetTextView, windTextView, humidityTextView;

    private LottieAnimationView loadingIconLottie, sunIconLottie, moonIconLottie, windIconLottie, humidityIconLottie;

    private Button getWeatherButton;

    public UIManager(Context context, View rootView) {
        this.context = context;
        this.rootView = rootView;
        initializeUI();
    }

    private void initializeUI() {
        cityInputEditText = rootView.findViewById(R.id.city_input_edit_text);
        cityTextView = rootView.findViewById(R.id.city_text_view);
        descriptionTextView = rootView.findViewById(R.id.description_text_view);

        sunriseLabelTextView = rootView.findViewById(R.id.sunrise_label_text_view);
        sunsetLabelTextView = rootView.findViewById(R.id.sunset_label_text_view);
        windLabelTextView = rootView.findViewById(R.id.wind_label_text_view);
        humidityLabelTextView = rootView.findViewById(R.id.humidity_label_text_view);

        getWeatherButton = rootView.findViewById(R.id.get_weather_button);

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

    // Setters and Getters for UI Components
    public void setCityLabel(City city) {
        cityTextView.setText(city.getCity().toUpperCase());
    }
    public Button getGetWeatherButton() {
        return getWeatherButton;
    }

    public String getCityName() {
        return cityInputEditText.getText().toString().trim().toLowerCase();
    }

    // UI Update Methods
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        cityInputEditText.clearFocus();
    }

    public void showLoadingIcon(boolean visible) {
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