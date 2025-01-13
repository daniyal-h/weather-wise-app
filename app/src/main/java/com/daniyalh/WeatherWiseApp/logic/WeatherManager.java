package com.daniyalh.WeatherWiseApp.logic;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daniyalh.WeatherWiseApp.data.DatabaseHelper;
import com.daniyalh.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;
import com.daniyalh.WeatherWiseApp.objects.City;
import com.daniyalh.WeatherWiseApp.objects.CityWeather;

public class WeatherManager implements IWeatherManager {
    public static final String API_KEY = "76c99c45ce84e16b80a83eaa2b188f38";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    private final RequestQueue requestQueue;
    private final WeatherJsonAdapter jsonAdapter;
    private final DatabaseHelper dbHelper;

    // inject context which indirectly injects the volley through overloading
    public WeatherManager(Context context) {
        this(Volley.newRequestQueue(context), new WeatherJsonAdapter());
    }

    // overloaded constructor
    public WeatherManager(RequestQueue requestQueue, WeatherJsonAdapter jsonAdapter) {
        this.requestQueue = requestQueue;
        this.jsonAdapter = jsonAdapter;
        dbHelper = DatabaseHelper.getInstance(null, "WeatherWiseApp.db");
        dbHelper.getWeatherRepository().setWeatherManager(this); // DBHelper always has latest WeatherManager
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
    public void setWeather(CityWeather city, String weatherJSON) {
        try {
            String[] weatherDetails = jsonAdapter.parseWeather(weatherJSON);
            city.updateWeather(weatherDetails);
        }
        catch (InvalidJsonParsingException e) {
            throw new InvalidJsonParsingException(e); // throw to layer that can handle it
        }
    }

    @Override
    public void getWeatherFromDB(City city, IWeatherDetailsCallback callback) {
        dbHelper.getWeatherRepository().getWeatherDetails(city, new IWeatherDetailsCallback() {
            @Override
            public void onSuccess(String[] weatherDetails) {
                callback.onSuccess(weatherDetails);
            }

            @Override
            public void onError(String error) {
                // Pass the error back to the UI layer via the callback
                callback.onError(error);
            }
        });
    }

    @Override
    public String[] fetchImmediateWeather(String weatherJSON) {
        return jsonAdapter.parseWeather(weatherJSON);
    }
}