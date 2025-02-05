package com.daniyalh.WeatherWiseApp.logic.forecast;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daniyalh.WeatherWiseApp.data.DatabaseHelper;
import com.daniyalh.WeatherWiseApp.objects.City;
import com.daniyalh.WeatherWiseApp.objects.Forecast;

public class ForecastManager implements IForecastManager {
    private final RequestQueue requestQueue;
    private final ForecastJsonAdapter forecastJsonAdapter;
    private final DatabaseHelper dbHelper;
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast?q=";
    private static final String API_KEY = "76c99c45ce84e16b80a83eaa2b188f38";

    public ForecastManager(Context context) {
        this(Volley.newRequestQueue(context), new ForecastJsonAdapter(), DatabaseHelper.getInstance());
    }

    public ForecastManager(RequestQueue requestQueue, ForecastJsonAdapter forecastJsonAdapter, DatabaseHelper dbHelper) {
        this.requestQueue = requestQueue;
        this.forecastJsonAdapter = forecastJsonAdapter;
        this.dbHelper = dbHelper;
        dbHelper.getForecastRepository().setForecastManager(this);
    }

    @Override
    public void getForecastFromDB(City city, IForecastDetailsCallback callback) {
        // link to DB
        dbHelper.getForecastRepository().getForecastDetails(city, callback);
    }

    @Override
    public void getForecastJSON(City city, IForecastCallback callback) {
        // return the API call through a callback
        String url = BASE_URL + city.getCityName() + "," + city.getCountryCode() + "&units=metric&appid=" + API_KEY;

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                callback::onSuccess,
                error -> callback.onError(error.toString()));

        requestQueue.add(stringRequest);
    }

    @Override
    public Forecast[] parseJSON(String forecastJSON) {
        return forecastJsonAdapter.parseForecast(forecastJSON);
    }
}
