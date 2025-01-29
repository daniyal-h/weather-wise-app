package com.daniyalh.WeatherWiseApp.objects;

import static org.junit.Assert.*;
import org.junit.Test;

public class ForecastTest {
    @Test
    public void testForecast() {
        System.out.println("\n----- Starting ForecastTest -----");

        Forecast forecast = new Forecast();
        String[] forecastDetails = new String[]{"0", "-10", "Clear", "Really clear", "15", "Mon, 12 pm", "false"};

        forecast.updateForecast(forecastDetails);
        assertEquals("Clear", forecast.getDescription());
        assertEquals("Mon, 12 pm", forecast.getDateTime());
        assertEquals(0.0, forecast.getTemp(), 0);
        assertEquals(-10.0, forecast.getFeelsLike(), 0);
        assertEquals(15.0*3.6, forecast.getWindSpeed(), 0.01);
        assertFalse(forecast.isDay());

        forecastDetails = new String[]{"0", "-10", "Atmosphere", "Really clear", "15", "Mon, 12 pm", "false"};
        forecast.updateForecast(forecastDetails);
        assertEquals("Really clear", forecast.getDescription());
        assertEquals("Mon, 12 pm", forecast.getDateTime());
        assertEquals(0.0, forecast.getTemp(), 0);
        assertEquals(-10.0, forecast.getFeelsLike(), 0);
        assertEquals(15.0*3.6, forecast.getWindSpeed(), 0.01);
        assertFalse(forecast.isDay());

        System.out.println("----- Finished ForecastTest -----\n");
    }
}
