package com.daniyalh.WeatherWiseApp.objects;

import static com.daniyalh.WeatherWiseApp.objects.Weather.MPS_TO_KMPH;

public class Forecast {
    private final int cityID, timezoneOffset;
    private String description;
    Long lastUpdated;
    private double temp, feelsLike, windSpeed;

    public Forecast(int cityID, int timezoneOffset) {
        this.cityID = cityID;
        this.timezoneOffset = timezoneOffset;
    }

    public void updateForecast(String[] forecastDetails) {
        lastUpdated = Long.parseLong(forecastDetails[0]);
        temp = Integer.parseInt(forecastDetails[1]);
        feelsLike = Integer.parseInt(forecastDetails[2]);
        description = forecastDetails[3];
        windSpeed = Math.round(Double.parseDouble(forecastDetails[5]) * MPS_TO_KMPH);
    }
}
