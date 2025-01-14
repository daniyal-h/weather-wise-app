package com.daniyalh.WeatherWiseApp.presentation.forecast;

import android.util.Log;

import com.daniyalh.WeatherWiseApp.logic.forecast.ForecastManager;
import com.daniyalh.WeatherWiseApp.logic.forecast.IForecastManager;
import com.daniyalh.WeatherWiseApp.objects.City;
import com.daniyalh.WeatherWiseApp.objects.Forecast;
import com.daniyalh.WeatherWiseApp.presentation.weather.WeatherController;

public class ForecastController {
    private final ForecastDetailActivity context;
    private final ForecastManager forecastManager;

    public ForecastController(ForecastDetailActivity context, ForecastManager forecastManager) {
        this.context = context;
        this.forecastManager = forecastManager;
    }

    public void fetchForecast() {
        City city = WeatherController.getInstance().getCity();
        forecastManager.getForecastFromDB(city, new IForecastManager.IForecastDetailsCallback() {
            @Override
            public void onSuccess(Forecast[] forecasts) {
                context.showLoadingIcon(false);
                city.setForecast(forecasts);

                Forecast[] tests = city.getForecasts();

                String displayName = city.getCityName() + ", " + city.getCountryCode();
                context.setDisplayName(displayName);
                context.displayForecasts(forecasts);
            }

            @Override
            public void onError(String error) {
                // TODO
            }
        });
    }
}
