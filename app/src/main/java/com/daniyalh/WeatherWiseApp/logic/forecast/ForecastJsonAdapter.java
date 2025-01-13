package com.daniyalh.WeatherWiseApp.logic.forecast;

import com.daniyalh.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;
import com.daniyalh.WeatherWiseApp.objects.Forecast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ForecastJsonAdapter implements IForecastJsonAdapter {
    @Override
    public Forecast[] parseForecast(String JSON) {
        Forecast[] forecasts = new Forecast[40];

        try {
            JSONObject root = new JSONObject(JSON);
            JSONArray forecastList = root.getJSONArray("list");
            int timezoneOffset = root.getJSONObject("city").getInt("timezone"); // Get city's timezone offset in seconds

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (int i = 0; i < forecastList.length(); i++) {
                JSONObject forecastObj = forecastList.getJSONObject(i);

                // Extract required details
                double temp = forecastObj.getJSONObject("main").getDouble("temp");
                double feelsLike = forecastObj.getJSONObject("main").getDouble("feels_like");
                String description = forecastObj.getJSONArray("weather").getJSONObject(0).getString("description");
                double windSpeed = forecastObj.getJSONObject("wind").getDouble("speed");

                // Extract UTC time and convert to local time
                String dateTime = forecastObj.getString("dt_txt"); // UTC time as string
                LocalDateTime utcDateTime = LocalDateTime.parse(dateTime, formatter);
                ZonedDateTime localDateTime = utcDateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneOffset.ofTotalSeconds(timezoneOffset));

                String localDate = localDateTime.toLocalDate().toString(); // Extract local date
                String localTime = localDateTime.toLocalTime().toString(); // Extract local time

                // Fill forecast details array
                String[] forecastDetails = {
                        String.valueOf(temp),
                        String.valueOf(feelsLike),
                        description,
                        String.valueOf(windSpeed),
                        localDate,
                        localTime
                };

                // Create and update Forecast object
                forecasts[i] = new Forecast();
                forecasts[i].updateForecast(forecastDetails);
            }
            } catch (Exception e) {
                throw new InvalidJsonParsingException(e);
            }

            return forecasts;
    }
}
