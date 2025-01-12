package com.daniyalh.WeatherWiseApp.objects;

public class City {
    private final int cityID;
    private String cityName, country, countryCode;
    private Weather currentWeather;
    private Forecast[] extendedForecast; // Array of 40 forecasts

    public City(int cityID) {
        this.cityID = cityID;
        this.extendedForecast = new Forecast[40]; // Initialize forecast array
    }

    public void setDetails(String cityName, String country, String countryCode) {
        this.cityName = cityName;
        this.country = country;
        this.countryCode = countryCode;
    }

    public void setWeather(Weather weather) {
        this.currentWeather = weather;
    }

    public void setForecast(Forecast[] forecast) {
        this.extendedForecast = forecast;
    }

    public int getCityID() {
        return cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Weather getCurrentWeather() {
        return currentWeather;
    }

    public Forecast[] getExtendedForecast() {
        return extendedForecast;
    }
}

