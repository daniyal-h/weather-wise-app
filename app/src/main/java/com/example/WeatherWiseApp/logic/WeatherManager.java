// WeatherManager.java
package com.example.WeatherWiseApp.logic;

import android.content.Context;
import android.net.Uri;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;
import com.example.WeatherWiseApp.objects.City;

public class WeatherManager implements IWeatherManager {

    private static final String API_KEY = "76c99c45ce84e16b80a83eaa2b188f38";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Override
    public void getWeatherJSON(Context context, City city, final IWeatherCallback callback) {
        // URL-encode the city name to handle spaces and special characters
        String url = BASE_URL + "?q=" + Uri.encode(city.getCity()) + "&appid=" + API_KEY;

        // Create a StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Pass the response to the callback
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Pass the error message to the callback
                        callback.onError(error.toString());
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(context).add(stringRequest);
    }

    @Override
    public void setWeather(City city, String weatherJSON) {
        WeatherJsonAdapter jsonAdapter = new WeatherJsonAdapter();
        String[] weatherDetails = jsonAdapter.parseWeather(weatherJSON);
        city.updateWeather(weatherDetails);
    }
}