package com.daniyalh.WeatherWiseApp.objects;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Weather {
    public static final double MPS_TO_KMPH = 3.6;

    private final String description, sunrise, sunset;
    private final int humidity, timezoneOffset;
    Long lastUpdated;
    private final double temp, feelsLike, windSpeed;
    private final char timeOfDay;

    public Weather(String[] weatherDetails) {
        // atomically update each weather attribute when called
        lastUpdated = Long.parseLong(weatherDetails[0]);
        temp = Math.round(Double.parseDouble(weatherDetails[1]));
        feelsLike = Math.round(Double.parseDouble(weatherDetails[2]));
        description = weatherDetails[3];
        humidity = Integer.parseInt(weatherDetails[4]);
        windSpeed = Math.round(Double.parseDouble(weatherDetails[5]) * MPS_TO_KMPH);
        timezoneOffset = Integer.parseInt(weatherDetails[6]);
        sunrise = timeStampToTime(Long.parseLong(weatherDetails[7]));
        sunset = timeStampToTime(Long.parseLong(weatherDetails[8]));
        timeOfDay = weatherDetails[9].charAt(0); // is either 'd' or 'n'
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

    // Getters

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

    public String getDescription() {
        return description;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getTimezoneOffset() {
        return timezoneOffset;
    }

    public char getTimeOfDay() {
        return timeOfDay;
    }
}

