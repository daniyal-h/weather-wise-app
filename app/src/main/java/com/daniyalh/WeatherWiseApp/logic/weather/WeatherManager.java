package com.daniyalh.WeatherWiseApp.logic.weather;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daniyalh.WeatherWiseApp.data.DatabaseHelper;
import com.daniyalh.WeatherWiseApp.objects.City;

public class WeatherManager implements IWeatherManager {
    public static final String API_KEY = "76c99c45ce84e16b80a83eaa2b188f38";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    private final RequestQueue requestQueue;
    private final WeatherJsonAdapter weatherJsonAdapter;
    private final DatabaseHelper dbHelper;

    // inject context which indirectly injects the volley through overloading
    public WeatherManager(Context context) {
        this(Volley.newRequestQueue(context), new WeatherJsonAdapter());
    }

    // overloaded constructor
    public WeatherManager(RequestQueue requestQueue, WeatherJsonAdapter weatherJsonAdapter) {
        this.requestQueue = requestQueue;
        this.weatherJsonAdapter = weatherJsonAdapter;
        dbHelper = DatabaseHelper.getInstance();
        dbHelper.getWeatherRepository().setWeatherManager(this); // DBHelper always has latest WeatherManager
    }

    @Override
    public void getWeatherFromDB(City city, IWeatherDetailsCallback callback) {
        // link to DB
        dbHelper.getWeatherRepository().getWeatherDetails(city, callback);
    }

    @Override
    public void getWeatherJSON(City city, IWeatherManager.IWeatherCallback callback) {
        String url = BASE_URL + "?q=" + city.getCityName() + "," + city.getCountryCode() + "&units=metric&appid=" + API_KEY;

        // Create a StringRequest
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                callback::onSuccess,
                error -> callback.onError(error.toString()));

        // Add the request to the RequestQueue
        requestQueue.add(stringRequest);
    }

    @Override
    public String[] fetchImmediateWeather(String weatherJSON) {
        return weatherJsonAdapter.parseWeather(weatherJSON);
    }
}