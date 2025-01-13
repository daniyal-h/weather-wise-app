package com.daniyalh.WeatherWiseApp.logic;

import static org.junit.Assert.assertEquals;
import android.database.Cursor;

import com.daniyalh.WeatherWiseApp.data.DatabaseHelper;

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
        ISearchManager.SearchCallback callback = Mockito.mock(ISearchManager.SearchCallback.class);

        Cursor dummyCursor = Mockito.mock(Cursor.class);
        Mockito.when(dummyCursor.getCount()).thenReturn(4);  // Simulate 4 results

        Mockito.when(mockDb.getCityRepository().getCitiesByQuery("New")).thenReturn(dummyCursor);

        SearchManager manager = new SearchManager(mockDb);

        manager.searchCities("New", callback); // should return 4

        // Capture the argument passed to onResults.
        ArgumentCaptor<Cursor> cursorCaptor = ArgumentCaptor.forClass(Cursor.class);
        Mockito.verify(callback).onResults(cursorCaptor.capture());

        // Verify that the cursor has 4 results
        Cursor capturedCursor = cursorCaptor.getValue();
        assertEquals("Expected 4 results", 4, capturedCursor.getCount());

        System.out.println("Finished testSearchCitiesWithValidInput successfully.\n");
    }
}
