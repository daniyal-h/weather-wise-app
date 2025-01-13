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

                for (Forecast test : tests) {
                    Log.d("ForecastController", test.getDate() + " " + test.getTime() + " Feels Like: " + test.getFeelsLike());
                }
                Long currentTime = System.currentTimeMillis();
                Log.d("ForecastController", "Last Updated: " + ((currentTime - city.getForecastLastUpdate()) / 60000) + " minutes ago");
            }

            @Override
            public void onError(String error) {
                // TODO
            }
        });
    }
}
