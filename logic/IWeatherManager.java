package logic;

import objects.City;

public interface IWeatherManager {
    String getWeatherJSON(City city);
    void setWeather(City city, String weatherJSON);
}
