package logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import objects.City;

public class WeatherManager implements IWeatherManager {
    private static final String API_KEY = "76c99c45ce84e16b80a83eaa2b188f38";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Override
    public String getWeatherJSON(City city) {
        String urlString = BASE_URL + "?q=" + city.getCity() + "&appid=" + API_KEY; 
        StringBuilder data =  new StringBuilder();

        try {
            URL url = new URL(urlString);

            // open connection
            HttpURLConnection connection = (HttpsURLConnection) url.openConnection();

            // Read the Response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
            reader.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return data.toString();
    }

    @Override
    public void setWeather(City city, String weatherJSON) {
        //JSONObject parser = new JSONObject();
    }
}
