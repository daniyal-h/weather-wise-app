package com.daniyalh.WeatherWiseApp.logic.forecast;

import androidx.annotation.NonNull;

import com.daniyalh.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;
import com.daniyalh.WeatherWiseApp.objects.Forecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ForecastJsonAdapter implements IForecastJsonAdapter {
    @Override
    public Forecast[] parseForecast(String JSON) {
        /*
        Return an array of 40 Forecasts
        Should cover 5 days with 3 hour jumps
         */
        Forecast[] forecasts = new Forecast[40];

        try {
            JSONObject root = new JSONObject(JSON);
            JSONArray forecastList = root.getJSONArray("list");
            int timezoneOffset = root.getJSONObject("city").getInt("timezone"); // Get city's timezone offset in seconds

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (int i = 0; i < forecastList.length(); i++) {
                JSONObject forecastObj = forecastList.getJSONObject(i);

                // Extract required details
                String[] forecastDetails =
                        extractForecastDetails(timezoneOffset, formatter, forecastObj);

                // Create and update Forecast object
                forecasts[i] = new Forecast();
                forecasts[i].updateForecast(forecastDetails);
            }
        } catch (JSONException e) {
            throw new InvalidJsonParsingException(e);
        }

        return forecasts;
    }

    @NonNull
    private static String[] extractForecastDetails(int timezoneOffset, DateTimeFormatter formatter, JSONObject forecastObj) throws JSONException {
        double temp = forecastObj.getJSONObject("main").getDouble("temp");
        double feelsLike = forecastObj.getJSONObject("main").getDouble("feels_like");
        String mainDescription = forecastObj.getJSONArray("weather").getJSONObject(0).getString("main");
        String detailedDescription = forecastObj.getJSONArray("weather").getJSONObject(0).getString("description");
        double windSpeed = forecastObj.getJSONObject("wind").getDouble("speed");

        // Extract UTC time and convert to local time
        String dateTime = forecastObj.getString("dt_txt"); // UTC time as string
        LocalDateTime utcDateTime = LocalDateTime.parse(dateTime, formatter);
        ZonedDateTime localDateTime = utcDateTime.atZone(ZoneOffset.UTC)
                .withZoneSameInstant(ZoneOffset.ofTotalSeconds(timezoneOffset));

        // Format the local date-time as "Mon at 12 pm"
        DateTimeFormatter simplifiedFormatter = DateTimeFormatter.ofPattern("EEE', 'h a", Locale.ENGLISH);
        String formattedDateTime = localDateTime.format(simplifiedFormatter).toLowerCase(); // Ensure lowercase "am/pm"

        // Capitalize the first letter of the day
        formattedDateTime = formattedDateTime.substring(0, 1).toUpperCase() + formattedDateTime.substring(1);

        // Use the "pod" field to determine if it's day or night
        String pod = forecastObj.getJSONObject("sys").getString("pod");
        boolean isDay = "d".equals(pod);

        // Fill forecast details array
        return new String[]{
                String.valueOf(temp),
                String.valueOf(feelsLike),
                mainDescription,
                detailedDescription,
                String.valueOf(windSpeed),
                formattedDateTime,
                String.valueOf(isDay)
        };
    }
}
