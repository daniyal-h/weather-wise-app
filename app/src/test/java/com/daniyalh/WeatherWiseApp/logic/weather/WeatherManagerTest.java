package com.daniyalh.WeatherWiseApp.logic.weather;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.daniyalh.WeatherWiseApp.data.DatabaseHelper;
import com.daniyalh.WeatherWiseApp.data.repositories.WeatherRepository;
import com.daniyalh.WeatherWiseApp.logic.exceptions.InvalidJsonParsingException;
import com.daniyalh.WeatherWiseApp.logic.weather.IWeatherManager;
import com.daniyalh.WeatherWiseApp.logic.weather.WeatherJsonAdapter;
import com.daniyalh.WeatherWiseApp.logic.weather.WeatherManager;
import com.daniyalh.WeatherWiseApp.objects.City;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    private WeatherRepository mockWeatherRepository;
    @Mock
    private RequestQueue mockRequestQueue;
    @Mock
    private WeatherJsonAdapter mockWeatherJsonAdapter;
    @Mock
    private IWeatherManager.IWeatherDetailsCallback mockCallback;
    @Mock
    private IWeatherManager.IWeatherCallback mockCallback2;
    @Captor
    private ArgumentCaptor<StringRequest> stringRequestCaptor;

    private WeatherManager weatherManager;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockWeatherJsonAdapter = mock(WeatherJsonAdapter.class);

        // inject mocked db too
        DatabaseHelper mockDb = Mockito.mock(DatabaseHelper.class);
        mockWeatherRepository = Mockito.mock(WeatherRepository.class);
        Mockito.when(mockDb.getWeatherRepository()).thenReturn(mockWeatherRepository);

        weatherManager = new WeatherManager(mockRequestQueue, mockWeatherJsonAdapter, mockDb);
    }

    @Test
    public void testGetWeatherFromDB_Success() {
        System.out.println("Starting testGetWeatherFromDB_Success...");

        // Arrange
        City city = new City(1);
        city.setDetails("TestCity", "TC");
        String[] weatherDetails = {"Sunny", "25°C", "1013 hPa"};

        // Simulate repository returning data successfully
        doAnswer(invocation -> {
            IWeatherManager.IWeatherDetailsCallback callback = invocation.getArgument(1);
            callback.onSuccess(weatherDetails);
            return null;
        }).when(mockWeatherRepository).getWeatherDetails(eq(city), any(IWeatherManager.IWeatherDetailsCallback.class));

        // Act
        weatherManager.getWeatherFromDB(city, mockCallback);

        // Assert
        verify(mockWeatherRepository).getWeatherDetails(eq(city), any(IWeatherManager.IWeatherDetailsCallback.class));
        verify(mockCallback).onSuccess(weatherDetails);
        verify(mockCallback, never()).onError(anyString());

        System.out.println("Finished testGetWeatherFromDB_Success successfully.\n");
    }

    @Test
    public void testGetWeatherFromDB_Error() {
        System.out.println("Starting testGetWeatherFromDB_Error...");

        // Arrange
        City city = new City(1);
        city.setDetails("TestCity", "TC");
        String errorMessage = "Database error"; // Simulated error message

        // Simulate repository failure by calling onError()
        doAnswer(invocation -> {
            IWeatherManager.IWeatherDetailsCallback callback = invocation.getArgument(1);
            callback.onError(errorMessage);
            return null;
        }).when(mockWeatherRepository).getWeatherDetails(eq(city), any(IWeatherManager.IWeatherDetailsCallback.class));

        // Act
        weatherManager.getWeatherFromDB(city, mockCallback);

        // Assert
        verify(mockWeatherRepository).getWeatherDetails(eq(city), any(IWeatherManager.IWeatherDetailsCallback.class)); // Ensure method was called
        verify(mockCallback).onError(errorMessage); // Ensure the error callback was triggered
        verify(mockCallback, never()).onSuccess(any()); // Ensure success callback was **not** triggered

        System.out.println("Finished testGetWeatherFromDB_Error successfully.\n");
        System.out.println("----- Finished WeatherManagerTest -----\n");
    }

    @Test
    public void testGetWeatherJSON() {
        System.out.println("----- Starting WeatherManagerTest -----\n");
        System.out.println("Starting testGetWeatherJSON...");
        // Arrange
        City city = new City(1);
        String expectedResponse = "{\"weather\":\"sunny\"}";

        // Act
        weatherManager.getWeatherJSON(city, mockCallback2);

        // Capture the StringRequest added to the RequestQueue
        verify(mockRequestQueue).add(stringRequestCaptor.capture());
        StringRequest capturedRequest = stringRequestCaptor.getValue();

        // Simulate a successful response by invoking the listener
        invokeOnResponse(capturedRequest, expectedResponse);

        // Assert
        verify(mockCallback2).onSuccess(expectedResponse);
        verify(mockCallback2, never()).onError(any());

        System.out.println("Finished testGetWeatherJSON successfully.\n");
    }


    @Test
    public void testSetWeather_ValidJSON() throws InvalidJsonParsingException {
        System.out.println("Starting testSetWeather_ValidJSON...");

        // Arrange
        String sampleJson = "{"
                + "\"main\":{"
                +     "\"temp\":20.44,"
                +     "\"feels_like\":18.51,"
                +     "\"humidity\":56"
                + "},"
                + "\"weather\":[{"
                +     "\"description\":\"Rain\""
                +     "\"description\":\"light rain\", \"icon\":\"01n\""
                + "}],"
                + "\"wind\":{"
                +     "\"speed\":4.1"
                + "},"
                + "\"timezone\":3600,"
                + "\"sys\":{"
                +     "\"country\":\"US\","
                +     "\"sunrise\":1627893600,"
                +     "\"sunset\":1627947600"
                + "}"
                + "}";

        String[] weatherDetails = {"9999999999", "20.44", "18.51", "Rain: light rain", "56", "4.1", "3600", "1627893600", "1627947600", "n"};

        when(mockWeatherJsonAdapter.parseWeather(anyString())).thenReturn(weatherDetails);
        String[] parsedWeather = weatherManager.fetchImmediateWeather(sampleJson);
        verify(mockWeatherJsonAdapter).parseWeather(sampleJson);

        assertNotNull(parsedWeather);
        assertEquals(20.44, Double.parseDouble(parsedWeather[1]), 0);
        assertEquals(18.51, Double.parseDouble(parsedWeather[2]), 0);
        assertEquals("Rain: light rain", parsedWeather[3]);
        assertEquals("56", parsedWeather[4]);
        assertEquals("4.1", parsedWeather[5]);
        assertEquals("3600", parsedWeather[6]);
        assertEquals("1627893600", parsedWeather[7]);
        assertEquals("1627947600", parsedWeather[8]);
        assertEquals("n", parsedWeather[9]);

        System.out.println("Finished testSetWeather_ValidJSON successfully.\n");
    }

    @Test
    public void testSetWeather_InvalidJSON() {
        System.out.println("Starting testSetWeather_InvalidJSON...");
        System.out.println("Expecting 1 error to be thrown...");

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
                // Missing closing braces and country
                ;

        // Mock parseWeather to throw exception for malformed JSON
        when(mockWeatherJsonAdapter.parseWeather(malformedJson))
                .thenThrow(new InvalidJsonParsingException(new Throwable()));

        // Act & Assert
        try {
            weatherManager.fetchImmediateWeather(malformedJson);
            fail("Expected InvalidJsonParsingException to be thrown");
        }
        catch (InvalidJsonParsingException e) {
            assertEquals("Unable to process weather data. Please try again.", e.getMessage());
            System.out.println("Exception thrown!");
        }

        // Verify that parseWeather was actually called inside setWeather()
        verify(mockWeatherJsonAdapter).parseWeather(malformedJson);

        System.out.println("Finished testSetWeather_InvalidJSON. All exceptions were successfully thrown.\n");
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
