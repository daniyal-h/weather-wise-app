package com.example.WeatherWiseApp.logic;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.example.WeatherWiseApp.objects.City;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.*;
import android.content.Context;

import java.io.File;

public class WeatherManagerTest {

    //@Test
    /*public void testGetWeatherJSONSuccess() {
        // Mock Context
        Context mockContext = mock(Context.class);
        when(mockContext.getCacheDir()).thenReturn(new File("mockCacheDir"));

        WeatherManager weatherManager = new WeatherManager();

        // Test
        weatherManager.getWeatherJSON(mockContext, new City("TestCity"), new IWeatherCallback() {
            @Override
            public void onSuccess(String response) {
                assertNotNull(response);
            }

            @Override
            public void onError(String error) {
                fail("Error callback should not be triggered.");
            }
        });
    }*/
}

