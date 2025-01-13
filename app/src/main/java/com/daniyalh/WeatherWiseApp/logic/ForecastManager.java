package com.daniyalh.WeatherWiseApp.logic;

import static com.daniyalh.WeatherWiseApp.logic.WeatherManager.API_KEY;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daniyalh.WeatherWiseApp.data.DatabaseHelper;
import com.daniyalh.WeatherWiseApp.objects.City;
import com.daniyalh.WeatherWiseApp.objects.Forecast;

public class ForecastManager implements IForecastManager {
    private final RequestQueue requestQueue;
    private final WeatherJsonAdapter jsonAdapter;
    private final DatabaseHelper dbHelper;
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast?q=";

    public ForecastManager(Context context) {
        this(Volley.newRequestQueue(context), new WeatherJsonAdapter());
    }
    public ForecastManager(RequestQueue requestQueue, WeatherJsonAdapter jsonAdapter) {
        this.requestQueue = requestQueue;
        this.jsonAdapter = jsonAdapter;
        this.dbHelper = DatabaseHelper.getInstance(null, "WeatherWiseApp.db");
    }

    @Override
    public void getForecastJSON(City city, IForecastCallback callback) {
        String url =  BASE_URL + city.getCityName() + "," + city.getCountryCode() + "&units=metric&appid=" + API_KEY;

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                response -> {
                    Forecast[] forecasts = jsonAdapter.parseForecast(response); // Parse forecast data
                    city.setForecast(forecasts); // Update city's forecast
                    callback.onSuccess(forecasts);
                },
                error -> callback.onError(error.toString())
        );

        requestQueue.add(stringRequest);
    }
}
