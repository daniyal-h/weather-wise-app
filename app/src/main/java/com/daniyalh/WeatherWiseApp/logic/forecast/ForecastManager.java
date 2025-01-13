package com.daniyalh.WeatherWiseApp.logic.forecast;

import static com.daniyalh.WeatherWiseApp.logic.weather.WeatherManager.API_KEY;

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

    public ForecastManager(Context context) {
        this(Volley.newRequestQueue(context), new ForecastJsonAdapter());
    }

    public ForecastManager(RequestQueue requestQueue, ForecastJsonAdapter forecastJsonAdapter) {
        this.requestQueue = requestQueue;
        this.forecastJsonAdapter = forecastJsonAdapter;
        this.dbHelper = DatabaseHelper.getInstance();
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
