package com.daniyalh.WeatherWiseApp.objects;

import static com.daniyalh.WeatherWiseApp.objects.Weather.MPS_TO_KMPH;

public class Forecast {
    private String mainDescription, detailedDescription, dateTime;
    Long lastUpdated;
    private double temp, feelsLike, windSpeed;
    boolean isDay;

    public void updateForecast(String[] forecastDetails) {
        temp = Math.round(Double.parseDouble(forecastDetails[0]));
        feelsLike = Math.round(Double.parseDouble(forecastDetails[1]));
        mainDescription = forecastDetails[2];
        detailedDescription = forecastDetails[3];
        windSpeed = Math.round(Double.parseDouble(forecastDetails[4]) * MPS_TO_KMPH);
        dateTime = forecastDetails[5];
        isDay = Boolean.parseBoolean(forecastDetails[6]);
    }

    public String getDescription() {
        // return main description for all but atmosphere readings
        // return detailed description when it's atmosphere
        if (!mainDescription.equals("Atmosphere"))
            return mainDescription;
        else return detailedDescription;
    }

    public String getDateTime() {
        return dateTime;
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

    public boolean isDay() {
        return isDay;
    }
}