package com.daniyalh.WeatherWiseApp.objects;

import static com.daniyalh.WeatherWiseApp.objects.Weather.MPS_TO_KMPH;

public class Forecast {
    private String description, date, time;
    Long lastUpdated;
    private double temp, feelsLike, windSpeed;

    public void updateForecast(String[] forecastDetails) {
        temp = Math.round(Double.parseDouble(forecastDetails[0]));
        feelsLike = Math.round(Double.parseDouble(forecastDetails[1]));
        description = forecastDetails[2];
        windSpeed = Math.round(Double.parseDouble(forecastDetails[3]) * MPS_TO_KMPH);
        date = forecastDetails[4];
        time = forecastDetails[5];
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public double getTemp() {
        return temp;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public double getWindSpeed() {
        return windSpeed;
    }
}