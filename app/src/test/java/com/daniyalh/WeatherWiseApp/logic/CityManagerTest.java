package com.daniyalh.WeatherWiseApp.logic;

import org.junit.Test;
import static org.junit.Assert.*;

import com.daniyalh.WeatherWiseApp.objects.City;

public class CityManagerTest {
    CityManager cityManager = new CityManager();

    City city1 = new City("city a");
    City city2 = new City("city b");
    City city3 = new City("city c");
    City city4 = new City("city d");
    City city5 = new City("city e");
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
        assertEquals(city4, cityManager.getCity("city d"));
        assertTrue(cityManager.cityExists("city e"));

        cityManager.removeCity("city c");
        cityCount--;

        assertEquals(cities.length-1, cityManager.getCityCount());
        assertFalse(cityManager.cityExists("city c"));

        // "empty" values stored
        assertArrayEquals(emptyValues, cityManager.getCity("city e").getValues());

        // update with new values
        city5.updateWeather(weatherReadings);
        cityManager.addCity(city5); // adding to hashmap is an update

        // duplicate; count should remain unchanged
        assertEquals(cityCount, cityManager.getCityCount());

        // values should be updated
        assertEquals("CA", cityManager.getCity("city e").getCountry());
        assertArrayEquals(expectedDetails, cityManager.getCity("city e").getWeather());

        System.out.println("----- Finished CityManagerTest -----\n");
    }
}
