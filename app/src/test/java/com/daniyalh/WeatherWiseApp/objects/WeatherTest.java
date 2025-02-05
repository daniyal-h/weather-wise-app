package com.daniyalh.WeatherWiseApp.objects;

import static org.junit.Assert.*;
import org.junit.Test;

public class WeatherTest {
    @Test
    public void testWeather() {
        System.out.println("\n----- Starting WeatherTest -----");

        String[] weatherDetails = {"9999999999", "0", "100", "raining cats and dogs: help!", "99", "27.7778", "-21600", "1734877486", "1734906604", "d"};

        Weather weather = new Weather(weatherDetails);

        assertEquals(Long.valueOf(9999999999L), weather.getLastUpdated());
        assertEquals(0.0, weather.getTemp(), 0);
        assertEquals(100.0, weather.getFeelsLike(), 0);
        assertEquals("raining cats and dogs", weather.getMainDescription());
        assertEquals("help!", weather.getDetailedDescription());
        assertEquals(99, weather.getHumidity());
        assertEquals(27.7778*3.6, weather.getWindSpeed(), 0.01);
        assertEquals("08:24 a.m.", weather.getSunrise());
        assertEquals("04:30 p.m.", weather.getSunset());
        assertEquals(-21600, weather.getTimezoneOffset());
        assertTrue(weather.isDay());

        System.out.println("----- Finished WeatherTest -----\n");
    }
}
