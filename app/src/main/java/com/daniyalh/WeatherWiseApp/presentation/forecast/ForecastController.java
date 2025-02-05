package com.daniyalh.WeatherWiseApp.presentation.forecast;

import android.widget.Toast;

import com.daniyalh.WeatherWiseApp.logic.forecast.ForecastManager;
import com.daniyalh.WeatherWiseApp.logic.forecast.IForecastManager;
import com.daniyalh.WeatherWiseApp.objects.City;
import com.daniyalh.WeatherWiseApp.objects.Forecast;
import com.daniyalh.WeatherWiseApp.presentation.weather.WeatherController;

public class ForecastController {
    private final ForecastPage forecastPage;
    private final ForecastManager forecastManager;

    public ForecastController(ForecastPage forecastPage, ForecastManager forecastManager) {
        this.forecastPage = forecastPage;
        this.forecastManager = forecastManager;
    }

    public void fetchForecast() {
        City city = WeatherController.getInstance().getCity();
        forecastManager.getForecastFromDB(city, new IForecastManager.IForecastDetailsCallback() {
            @Override
            public void onSuccess(Forecast[] forecasts) {
                // set the forecasts to the city and display appropriately
                forecastPage.showLoadingIcon(false);
                city.setForecast(forecasts);
                forecastPage.displayForecasts(city);
            }

            @Override
            public void onError(String error) {
                forecastPage.showToast(error, Toast.LENGTH_SHORT);
            }
        });
    }
}
