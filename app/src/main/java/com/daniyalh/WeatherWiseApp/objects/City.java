package com.daniyalh.WeatherWiseApp.objects;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class City {
    private final String city;
    private String description;
    private String sunrise;
    private String sunset;
    private int humidity, timezoneOffset;
    private double temp, feelsLike, windSpeed;
    private char timeOfDay;

    private static final double K_TO_C = 273.15;
    private static final double MPS_TO_KMPH = 3.6;

    public City(String city) {
        this.city = city;
    }

    public void updateWeather(String[] weatherDetails) {
        // atomically update each weather attribute when called
        temp = Math.round(Double.parseDouble(weatherDetails[0]) - K_TO_C);
        feelsLike = Math.round(Double.parseDouble(weatherDetails[1]) - K_TO_C);
        description = weatherDetails[2];
        humidity = Integer.parseInt(weatherDetails[3]);
        windSpeed = Math.round(Double.parseDouble(weatherDetails[4]) * MPS_TO_KMPH);
        timezoneOffset = Integer.parseInt(weatherDetails[5]);
        sunrise = timeStampToTime(Long.parseLong(weatherDetails[6]));
        sunset = timeStampToTime(Long.parseLong(weatherDetails[7]));
        timeOfDay = weatherDetails[8].charAt(0); // is either 'd' or 'n'
    }

    private String timeStampToTime(long timestamp) {
        // convert time to instant
        Instant instant = Instant.ofEpochSecond(timestamp);

        // create offset
        ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(timezoneOffset);

        // convert instant to time
        ZonedDateTime time = ZonedDateTime.ofInstant(instant, zoneOffset);

        // return time formatted for readability
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return time.format(formatter);
    }

    public String getCity() {
        return city; // get city name
    }

    public String[] getWeather() {
        // return an array of strings that cover the weather details of the city
        String[] weather = new String[8];

        weather[0] = (int) temp + "°C";
        weather[1] = "Feels Like " + (int) feelsLike;
        weather[2] = description;
        weather[3] = humidity + "%";
        weather[4] = (int) windSpeed + " km/h";
        weather[5] = sunrise.toLowerCase();
        weather[6] = sunset.toLowerCase();
        weather[7] = String.valueOf(timeOfDay);

        return weather;
    }

    public String[] getValues() {
        // return an array of strings that cover the unconverted values of the city
        return new String[]{String.valueOf(temp), String.valueOf(feelsLike), String.valueOf(description),
                String.valueOf(humidity), String.valueOf(windSpeed), String.valueOf(timezoneOffset),
                String.valueOf(sunrise), String.valueOf(sunset), String.valueOf(timeOfDay)};
    }
}
