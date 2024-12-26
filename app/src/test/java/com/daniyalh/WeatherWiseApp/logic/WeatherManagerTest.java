package com.daniyalh.WeatherWiseApp.logic;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.daniyalh.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;
import com.daniyalh.WeatherWiseApp.objects.City;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
@SuppressWarnings("unchecked")
public class WeatherManagerTest {

    @Mock
    private RequestQueue mockRequestQueue;
    @Mock
    private WeatherJsonAdapter mockJsonAdapter;
    @Mock
    private IWeatherCallback mockCallback;
    @Captor
    private ArgumentCaptor<StringRequest> stringRequestCaptor;

    private WeatherManager weatherManager;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialize WeatherManager with mocked dependencies
        weatherManager = new WeatherManager(mockRequestQueue, mockJsonAdapter);
    }

    @Test
    public void testGetWeatherJSON() {
        System.out.println("----- Starting WeatherManagerTest -----\n");
        System.out.println("Starting testGetWeatherJSON...");
        // Arrange
        City city = new City("London");
        String expectedResponse = "{\"weather\":\"sunny\"}";

        // Act
        weatherManager.getWeatherJSON(city, mockCallback);

        // Capture the StringRequest added to the RequestQueue
        verify(mockRequestQueue).add(stringRequestCaptor.capture());
        StringRequest capturedRequest = stringRequestCaptor.getValue();

        // Simulate a successful response by invoking the listener
        invokeOnResponse(capturedRequest, expectedResponse);

        // Assert
        verify(mockCallback).onSuccess(expectedResponse);
        verify(mockCallback, never()).onError(any());

        System.out.println("Finished testGetWeatherJSON successfully.\n");
    }

    @Test
    public void testSetWeather_ValidJSON() throws InvalidJsonParsingException {
        System.out.println("Starting testSetWeather_ValidJSON...");

        // Arrange
        City city = new City("New York");
        String sampleJson = "{"
                + "\"main\":{"
                +     "\"temp\":293.15,"
                +     "\"feels_like\":291.86,"
                +     "\"humidity\":56"
                + "},"
                + "\"weather\":[{"
                +     "\"description\":\"light rain\", \"icon\":\"01n\""
                + "}],"
                + "\"wind\":{"
                +     "\"speed\":4.1"
                + "},"
                + "\"timezone\":3600,"
                + "\"sys\":{"
                +     "\"sunrise\":1627893600,"
                +     "\"sunset\":1627947600"
                + "}"
                + "}";

        String[] expectedWeatherDetails = {
                "293.15",          // Temperature (K)
                "291.86",          // Feels Like (K)
                "light rain",      // Description
                "56",              // Humidity (%)
                "4.1",             // Wind Speed (m/s)
                "3600",            // Timezone Offset
                "1627893600",      // Sunrise Timestamp
                "1627947600",      // Sunset Timestamp
                "n"                // nighttime
        };

        // Mock the parseWeather to return all 8 elements
        when(mockJsonAdapter.parseWeather(sampleJson)).thenReturn(expectedWeatherDetails);

        // Act
        weatherManager.setWeather(city, sampleJson);

        // Assert
        verify(mockJsonAdapter).parseWeather(sampleJson);

        String[] weather = city.getWeather();

        assertEquals("20°C", weather[0]);         // 293.15K -> 20°C
        assertEquals("Feels Like 19", weather[1]);          // 291.86K -> 19°C
        assertEquals("light rain", weather[2]);
        assertEquals("56%", weather[3]);
        assertEquals("15 km/h", weather[4]);       // 4.1 m/s -> 14 km/h
        assertEquals("09:40 am", weather[5]);
        assertEquals("12:40 am", weather[6]);
        assertEquals("n", weather[7]);

        System.out.println("Finished testSetWeather_ValidJSON successfully.\n");
    }

    @Test
    public void testSetWeather_InvalidJSON() {
        System.out.println("Starting testSetWeather_InvalidJSON...");
        System.out.println("Expecting 1 error to be thrown...");

        City city = new City("New York");

        // Example of malformed JSON (missing closing brace)
        String malformedJson = "{"
                + "\"main\":{"
                +     "\"temp\":293.15,"
                +     "\"feels_like\":291.86,"
                +     "\"humidity\":56"
                + "},"
                + "\"weather\":[{"
                +     "\"description\":\"light rain\""
                + "}],"
                + "\"wind\":{"
                +     "\"speed\":4.1"
                + "},"
                + "\"timezone\":3600,"
                + "\"sys\":{"
                +     "\"sunrise\":1627893600,"
                +     "\"sunset\":1627947600"
                // Missing closing braces
                ;

        // Mock parseWeather to throw exception for malformed JSON
        when(mockJsonAdapter.parseWeather(malformedJson))
                .thenThrow(new InvalidJsonParsingException(new Throwable()));

        // Act & Assert
        try {
            weatherManager.setWeather(city, malformedJson);
            fail("Expected InvalidJsonParsingException to be thrown");
        }
        catch (InvalidJsonParsingException e) {
            assertEquals("Unable to process weather data. Please try again.", e.getMessage());
            System.out.println("Exception thrown!");
        }

        System.out.println("Finished testSetWeather_InvalidJSON. All exceptions were successfully thrown.\n");
        System.out.println("----- Finished WeatherManagerTest -----\n");
    }

    private void invokeOnResponse(StringRequest request, String response) {
        try {
            Field listenerField = StringRequest.class.getDeclaredField("mListener");
            listenerField.setAccessible(true);
            Response.Listener<String> listener = (Response.Listener<String>) listenerField.get(request);
            listener.onResponse(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
