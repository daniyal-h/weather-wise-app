package com.example.WeatherWiseApp.objects;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class City {
    private String city, description, sunrise, sunset;
    private int humidity, timezoneOffset;
    private double temp, feelsLike, windSpeed;

    private static final double K_TO_C = 273.15;
    // String temp, feelsLike, description, windSpeed, humidity, pressure, clouds, sunrise, sunset;

    public City(String city) {
        this.city = city;
    }

    public void updateWeather(String[] weatherDetails) {
        // atomically update each weather attribute when called
        temp = Math.round((Double.parseDouble(weatherDetails[0]) - K_TO_C) * 100.0) / 100.0;
        feelsLike = Math.round((Double.parseDouble(weatherDetails[1]) - K_TO_C) * 100.0) / 100.0;
        description = weatherDetails[2];
        humidity = Integer.parseInt(weatherDetails[3]);
        windSpeed = Double.parseDouble(weatherDetails[4]);
        timezoneOffset = Integer.parseInt(weatherDetails[5]);
        sunrise = timeStampToTime(Long.parseLong(weatherDetails[6]));
        sunset = timeStampToTime(Long.parseLong(weatherDetails[7]));
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
        String[] weather = new String[7];

        weather[0] = "Temperature: " + String.valueOf(temp) + "°C";
        weather[1] = "Feels Like: " + String.valueOf(feelsLike) + "°C";
        weather[2] = "Description: " + description;
        weather[3] = "Humidity: " + String.valueOf(humidity) + "%";
        weather[4] = "Wind Speed: " + String.valueOf(windSpeed) + " m/s";
        weather[5] = "Sunrise: " + sunrise;
        weather[6] = "Sunset: " + sunset;

        return weather;
    }
}
