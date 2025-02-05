package com.daniyalh.WeatherWiseApp.logic.home;

import static org.junit.Assert.assertEquals;
import android.database.Cursor;

import com.daniyalh.WeatherWiseApp.data.DatabaseHelper;
import com.daniyalh.WeatherWiseApp.data.repositories.CityRepository;
import com.daniyalh.WeatherWiseApp.logic.home.ISearchManager;
import com.daniyalh.WeatherWiseApp.logic.home.SearchManager;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class SearchManagerTest {

    @Test
    public void testSearchCitiesWithInvalidInput() {
        System.out.println("Starting testSearchCitiesWithInvalidInput...");
        // Create mocks
        DatabaseHelper mockDb = Mockito.mock(DatabaseHelper.class);
        ISearchManager.SearchCallback callback = Mockito.mock(ISearchManager.SearchCallback.class);

        SearchManager manager = new SearchManager(mockDb);

        // Test with null query
        manager.searchCities(null, callback);
        Mockito.verify(callback).onError("please enter a city");

        // Test with empty string query
        manager.searchCities("   ", callback);
        Mockito.verify(callback, Mockito.times(2)).onError("please enter a city");

        System.out.println("Finished testSearchCitiesWithInvalidInput successfully.\n");
        System.out.println("----- Finished SearchManagerTest -----\n");
    }

    @Test
    public void testSearchCitiesWithValidInput() {
        System.out.println("----- Starting SearchManagerTest -----\n");
        System.out.println("Starting testSearchCitiesWithValidInput...");
        // Create mocks
        DatabaseHelper mockDb = Mockito.mock(DatabaseHelper.class);
        CityRepository mockCityRepository = Mockito.mock(CityRepository.class);
        ISearchManager.SearchCallback callback = Mockito.mock(ISearchManager.SearchCallback.class);

        Mockito.when(mockDb.getCityRepository()).thenReturn(mockCityRepository);

        Cursor dummyCursor = Mockito.mock(Cursor.class);
        Mockito.when(dummyCursor.getCount()).thenReturn(5);  // Simulate 5 results

        Mockito.when(mockCityRepository.getCitiesByQuery("new y")).thenReturn(dummyCursor);

        SearchManager manager = new SearchManager(mockDb);

        manager.searchCities("new y", callback); // should return 5

        // Capture the argument passed to onResults.
        ArgumentCaptor<Cursor> cursorCaptor = ArgumentCaptor.forClass(Cursor.class);
        Mockito.verify(callback).onResults(cursorCaptor.capture());

        // Verify that the cursor has 5 results
        Cursor capturedCursor = cursorCaptor.getValue();
        assertEquals("Expected 5 results", 5, capturedCursor.getCount());

        System.out.println("Finished testSearchCitiesWithValidInput successfully.\n");
    }
}
