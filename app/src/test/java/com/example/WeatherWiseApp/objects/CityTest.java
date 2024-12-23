package com.example.WeatherWiseApp.objects;

import org.junit.Test;
import static org.junit.Assert.*;

public class CityTest {
    @Test
    public void testCity() {
        City city;

        System.out.println("\nStarting testCity...");

        city = new City("Winnipeg");

        String[] fixedDetails = {"273.15", "373.15", "raining cats and dogs", "100", "27.7778", "-21600", "1734877486", "1734906604"};

        city.updateWeather(fixedDetails);

        String[] details = city.getWeather();

        assertEquals("Temperature: 0°C", details[0]);
        assertEquals("Feels Like: 100°C", details[1]);
        assertEquals("Description: raining cats and dogs", details[2]);
        assertEquals("Humidity: 100%", details[3]);
        assertEquals("Wind Speed: 100 km/h", details[4]);
        assertEquals("Sunrise: 08:24 a.m.", details[5]);
        assertEquals("Sunset: 04:30 p.m.", details[6]);

        System.out.println("Finished testCity successfully.\n");
    }
}