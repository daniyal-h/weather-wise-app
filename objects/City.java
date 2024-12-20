package objects;

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

    public void updateWeather(double temp, double feelsLike, String description, int humidity, double windSpeed, int timezoneOffset, long sunrise, long sunset) {
        // atomically update each weather attribute when called
        this.temp = temp + K_TO_C;
        this.feelsLike = feelsLike + K_TO_C;
        this.description = description;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.timezoneOffset = timezoneOffset;
        this.sunrise = timeStampToTime(sunrise);
        this.sunset = timeStampToTime(sunset);
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

        weather[1] = String.valueOf(temp);
        weather[2] = String.valueOf(feelsLike);
        weather[3] = description;
        weather[4] = String.valueOf(humidity);
        weather[5] = String.valueOf(windSpeed);
        weather[6] = sunrise;
        weather[7] = sunset;

        return weather;
    }
}
