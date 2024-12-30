package com.daniyalh.WeatherWiseApp.logic;

import org.junit.Test;
import static org.junit.Assert.*;

import com.daniyalh.WeatherWiseApp.objects.City;

public class CityManagerTest {
    CityManager cityManager = new CityManager();

    City city1 = new City(1);
    City city2 = new City(2);
    City city3 = new City(3);
    City city4 = new City(4);
    City city5 = new City(5);
    City[] cities = {city1, city2, city3, city4, city5};
    int cityCount = 0;

    String[] emptyValues = new String[]
            {"0.0", "0.0", "null", "0", "0.0", "0", "null", "null", "\u0000"};

    String[] weatherReadings = new String[]
            {"CA", "0", "1", "OCD satisfied", "50", "27.777778", "0", "0", "0", "n"};

    String[] expectedDetails = new String[]
            {"0°C", "Feels Like 1", "OCD satisfied", "50%",
                    "100 km/h", "12:00 a.m.", "12:00 a.m.", "n"};

    @Test
    public void testCityManager() {
        System.out.println("----- Starting CityManagerTest -----");

        for (City city : cities) {
            cityManager.addCity(city);
            cityCount++;
        }

        assertEquals(cityCount, cityManager.getCityCount());
        assertEquals(city4, cityManager.getCity(4));
        assertTrue(cityManager.cityExists(5));

        cityManager.removeCity(3);
        cityCount--;

        assertEquals(cities.length-1, cityManager.getCityCount());
        assertFalse(cityManager.cityExists(3));

        // "empty" values stored
        assertArrayEquals(emptyValues, cityManager.getCity(5).getValues());

        // update with new values
        city5.updateWeather(weatherReadings);
        cityManager.addCity(city5); // adding to hashmap is an update

        // duplicate; count should remain unchanged
        assertEquals(cityCount, cityManager.getCityCount());

        // values should be updated
        assertEquals("CA", cityManager.getCity(5).getCountryCode());
        assertArrayEquals(expectedDetails, cityManager.getCity(5).getWeather());

        System.out.println("----- Finished CityManagerTest -----\n");
    }
}
