package com.daniyalh.WeatherWiseApp.objects;

import org.junit.Test;
import static org.junit.Assert.*;

public class CityTest {
    @Test
    public void testCity() {
        City city;

        System.out.println("\n----- Starting CityTest -----");

        city = new City("Winnipeg");

        String[] fixedDetails = {"EX", "0", "100", "raining cats and dogs", "100", "27.7778", "-21600", "1734877486", "1734906604", "d"};

        city.updateWeather(fixedDetails);

        String[] details = city.getWeather();

        assertEquals("EX", city.getCountryCode());
        assertEquals("0°C", details[0]);
        assertEquals("Feels Like 100", details[1]);
        assertEquals("raining cats and dogs", details[2]);
        assertEquals("100%", details[3]);
        assertEquals("100 km/h", details[4]);
        assertEquals("08:24 a.m.", details[5]);
        assertEquals("04:30 p.m.", details[6]);
        assertEquals("d", details[7]);

        System.out.println("----- Finished CityTest -----\n");
    }
}
