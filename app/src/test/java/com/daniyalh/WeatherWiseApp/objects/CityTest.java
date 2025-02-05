package com.daniyalh.WeatherWiseApp.objects;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Optional;

public class CityTest {
    @Test
    public void testCity() {
        City city;

        System.out.println("\n----- Starting CityTest -----");

        city = new City(1);
        city.setDetails("New York", "US");
        city.setForecastLastUpdate(9223372036854775807L);

        assertEquals(1, city.getCityID());
        assertEquals("New York", city.getCityName());
        assertEquals("US", city.getCountryCode());
        assertEquals(Long.valueOf(9223372036854775807L), city.getForecastLastUpdate());
        assertNull(city.getCurrentWeather());
        assertNotNull(city.getForecasts());

        System.out.println("----- Finished CityTest -----\n");
    }
}
