package com.example.WeatherWiseApp.logic;

import com.example.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;

import org.json.JSONObject;

public class WeatherJsonAdapter implements IWeatherJsonAdapter {
    @Override
    public String[] parseWeather(String JSON) throws InvalidJsonParsingException {
        String[] weatherDetails = new String[8];
        try {
            // Parse the JSON data
            JSONObject root = new JSONObject(JSON);

            // Fill the weather details array
            weatherDetails[0] = String.valueOf(root.getJSONObject("main").getDouble("temp"));        // Temperature
            weatherDetails[1] = String.valueOf(root.getJSONObject("main").getDouble("feels_like"));  // Feels Like
            weatherDetails[2] = root.getJSONArray("weather").getJSONObject(0).getString("description"); // Description
            weatherDetails[3] = String.valueOf(root.getJSONObject("main").getInt("humidity"));       // Humidity
            weatherDetails[4] = String.valueOf(root.getJSONObject("wind").getDouble("speed"));       // Wind Speed
            weatherDetails[5] = String.valueOf(root.getInt("timezone"));                                   // Timezone Offset
            weatherDetails[6] = String.valueOf(root.getJSONObject("sys").getLong("sunrise"));        // Sunrise Timestamp
            weatherDetails[7] = String.valueOf(root.getJSONObject("sys").getLong("sunset"));         // Sunset Timestamp
        }
        catch (Exception e) {
            throw new InvalidJsonParsingException(e);
        }

        return weatherDetails;
    }
}
