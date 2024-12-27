package com.daniyalh.WeatherWiseApp.logic;

import org.junit.Test;
import static org.junit.Assert.*;

import com.daniyalh.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;

public class WeatherJsonAdapterTest {
    WeatherJsonAdapter jsonAdapter = new WeatherJsonAdapter();
    String[] weatherDetails;
    @Test
    public void testWeatherJsonAdapter() {
        System.out.println("----- Starting WeatherJsonAdapterTest -----");

        validJsonParsing();
        invalidJsonParsing();

        System.out.println("----- Finished WeatherJsonAdapterTest -----\n");
    }

    private void validJsonParsing() {
        System.out.println("\nStarting validJSONParsing...");

        String validJSON1 = "{\"coord\":{\"lon\":-74.006,\"lat\":40.7143},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"base\":\"stations\",\"main\":{\"temp\":265.05,\"feels_like\":258.05,\"temp_min\":263.47,\"temp_max\":266.13,\"pressure\":1035,\"humidity\":52,\"sea_level\":1035,\"grnd_level\":1034},\"visibility\":10000,\"wind\":{\"speed\":5.81,\"deg\":109,\"gust\":8.94},\"clouds\":{\"all\":0},\"dt\":1734916301,\"sys\":{\"type\":2,\"id\":2008776,\"country\":\"US\",\"sunrise\":1734869839,\"sunset\":1734903140},\"timezone\":-18000,\"id\":5128581,\"name\":\"New York\",\"cod\":200}";
        String validJSON2 = "{\"coord\":{\"lon\":-99.1277,\"lat\":19.4285},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"base\":\"stations\",\"main\":{\"temp\":285.43,\"feels_like\":284.75,\"temp_min\":285.11,\"temp_max\":285.43,\"pressure\":1018,\"humidity\":78,\"sea_level\":1018,\"grnd_level\":765},\"visibility\":10000,\"wind\":{\"speed\":3.09,\"deg\":360},\"rain\":{\"1h\":0.25},\"clouds\":{\"all\":75},\"dt\":1734916894,\"sys\":{\"type\":2,\"id\":2097454,\"country\":\"MX\",\"sunrise\":1734872799,\"sunset\":1734912243},\"timezone\":-21600,\"id\":3530597,\"name\":\"Mexico City\",\"cod\":200}";

        String[] expectedResponse1 = new String[] {"US", "265.05", "258.05", "clear sky", "52", "5.81", "-18000", "1734869839", "1734903140", "n"};
        String[] expectedResponse2 = new String[] {"MX", "285.43", "284.75", "light rain", "78", "3.09", "-21600", "1734872799", "1734912243", "d"};

        weatherDetails = jsonAdapter.parseWeather(validJSON1);

        assertArrayEquals(expectedResponse1, weatherDetails);

        weatherDetails = jsonAdapter.parseWeather(validJSON2);
        assertArrayEquals(expectedResponse2, weatherDetails);

        System.out.println("Finished validJSONParsing successfully.");
    }

    private void invalidJsonParsing() {
        String BrokenJSON1 = "{\"coord\":{\"lon\":-99.1277,\"lat\":19.4285},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10n\"}],\"wind\":{\"speed\":3.09,\"deg\":360},\"sys\":{\"type\":2,\"id\":2097454,\"country\":\"MX\",\"sunrise\":1734872799,\"sunset\":1734912243},\"timezone\":-21600,\"id\":3530597,\"name\":\"Mexico City\",\"cod\":200}";
        String BrokenJSON2 = "{\"coord\":{\"lon\":-99.1277,\"lat\":19.4285},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10n\"}],\"main\":{\"temp\":\"invalid_temp\",\"feels_like\":284.75,\"temp_min\":285.11,\"temp_max\":285.43,\"pressure\":1018,\"humidity\":78},\"wind\":{\"speed\":3.09,\"deg\":360},\"sys\":{\"type\":2,\"id\":2097454,\"country\":\"MX\",\"sunrise\":1734872799,\"sunset\":1734912243},\"timezone\":-21600,\"id\":3530597,\"name\":\"Mexico City\",\"cod\":200}";
        String BrokenJSON3 = "{\"coord\":{\"lon\":-99.1277,\"lat\":19.4285},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10n\"}],\"main\":{\"temp\":285.43,\"feels_like\":284.75,\"temp_min\":285.11,\"temp_max\":285.43,\"pressure\":1018,\"humidity\":78,\"sea_level\":1018,\"grnd_level\":765},\"wind\":{\"speed\":3.09,\"deg\":360},\"sys\":{\"type\":2,\"id\":2097454,\"country\":\"MX\",\"sunrise\":1734872799,\"sunset\":1734912243},\"timezone\":-21600,\"id\":3530597,\"name\":\"Mexico City\",\"cod\":200";

        String[] brokenJSONs = {BrokenJSON1, BrokenJSON2, BrokenJSON3};

        int errorCounter = 0;

        System.out.println("\nStarting invalidJSONParsing...");
        System.out.println("Expecting " + brokenJSONs.length + " exceptions to be thrown...");

        for (String BROKEN_JSON : brokenJSONs) {
            try {
                jsonAdapter.parseWeather(BROKEN_JSON);
            } catch (InvalidJsonParsingException e) {
                System.out.println("Exception thrown!");
                errorCounter++;
            }
        }

        if (errorCounter == brokenJSONs.length) {
            System.out.println("Finished invalidJSONParsing. All exceptions were successfully thrown.\n");
        } else {
            System.err.println("Test failed: Expected " + brokenJSONs.length + " exceptions, but got " + errorCounter);
        }

        assertEquals(brokenJSONs.length, errorCounter);
    }
}
