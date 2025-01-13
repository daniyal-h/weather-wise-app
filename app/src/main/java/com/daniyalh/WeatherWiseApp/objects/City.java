package com.daniyalh.WeatherWiseApp.objects;

public class City {
    private final int cityID;
    private String cityName, country, countryCode;
    private Weather currentWeather;
    private long forecastLastUpdated;
    private Forecast[] forecasts; // Array of 40 forecasts

    public City(int cityID) {
        this.cityID = cityID;
        this.forecasts = new Forecast[40]; // Initialize forecast array
    }

    public void setDetails(String cityName, String country, String countryCode) {
        this.cityName = cityName;
        this.country = country;
        this.countryCode = countryCode;
    }

    public void setWeather(Weather weather) {
        this.currentWeather = weather;
    }

    public void setForecast(Forecast[] forecasts) {
        this.forecasts = forecasts;
    }

    public void setForecastLastUpdate(Long forecastLastUpdated) {
        this.forecastLastUpdated = forecastLastUpdated;
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

    public Long getForecastLastUpdate() {
        return forecastLastUpdated;
    }

    public Weather getCurrentWeather() {
        return currentWeather;
    }

    public Forecast[] getForecasts() {
        return forecasts;
    }
}

