package com.daniyalh.WeatherWiseApp.logic;

import com.daniyalh.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;
import org.json.JSONObject;

public class WeatherJsonAdapter implements IWeatherJsonAdapter {
    @Override
    public String[] parseWeather(String JSON) {
        String[] weatherDetails = new String[10];
        try {
            // Parse the JSON data
            JSONObject root = new JSONObject(JSON);

            //weatherDetails[0] = root.getJSONObject("sys").getString("country");                     // Country
            weatherDetails[1] = String.valueOf(root.getJSONObject("main").getDouble("temp"));       // Temperature
            weatherDetails[2] = String.valueOf(root.getJSONObject("main").getDouble("feels_like")); // Feels Like
            weatherDetails[3] = root.getJSONArray("weather").getJSONObject(0).getString("description"); // Description
            weatherDetails[4] = String.valueOf(root.getJSONObject("main").getInt("humidity"));      // Humidity
            weatherDetails[5] = String.valueOf(root.getJSONObject("wind").getDouble("speed"));      // Wind Speed
            weatherDetails[6] = String.valueOf(root.getInt("timezone"));                            // Timezone Offset
            weatherDetails[7] = String.valueOf(root.getJSONObject("sys").getLong("sunrise"));       // Sunrise Timestamp
            weatherDetails[8] = String.valueOf(root.getJSONObject("sys").getLong("sunset"));        // Sunset Timestamp

            String icon = root.getJSONArray("weather").getJSONObject(0).getString("icon"); // e.g., "02d" or "02n"
            weatherDetails[9] = icon.substring(icon.length() - 1);                         // Get the last character ('d' or 'n')
        }
        catch (Exception e) {
            throw new InvalidJsonParsingException(e);
        }

        return weatherDetails;
    }
}
