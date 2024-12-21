package presentation;

import logic.WeatherManager;
import objects.City;

public class Main {
    public static void main(String[] args) {
        CityProvider cityProvider = new CityProvider();
        WeatherManager weatherManager = new WeatherManager();

        City defaultCity = new City(cityProvider.getCity());
        System.out.println("The default city is: " + defaultCity.getCity());
        System.out.println(weatherManager.getWeatherJSON(defaultCity));
    }
}
