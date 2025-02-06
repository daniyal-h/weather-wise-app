package com.daniyalh.WeatherWiseApp.presentation.home;

import android.database.Cursor;

import com.daniyalh.WeatherWiseApp.logic.home.ISearchManager;
import com.daniyalh.WeatherWiseApp.logic.home.SearchManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class SearchHelperTest {
    @Mock
    private HomePage mockHomePage;
    @Mock
    private SearchManager mockSearchManager;
    @Mock
    private CityCursorAdapter mockCityCursorAdapter;
    @Mock
    private Cursor mockCursor;

    private SearchHelper searchHelper;

    @Before
    public void setUp() {
        // Initialize mock dependencies
        MockitoAnnotations.openMocks(this);

        // Inject mock HomePage
        searchHelper = new SearchHelper(mockHomePage);

        // Replace the real SearchManager and CityCursorAdapter with mocks
        ReflectionTestUtils.setField(searchHelper, "searchManager", mockSearchManager);
        ReflectionTestUtils.setField(searchHelper, "cityCursorAdapter", mockCityCursorAdapter);
    }

    @Test
    public void testDebounceSearch() {
        // Arrange
        String query = "New York";

        // Simulate a successful search result
        when(mockCursor.getCount()).thenReturn(1);
        when(mockCityCursorAdapter.swapCursor(mockCursor)).thenReturn(null);

        // Act: Call debounceSearch (which should delay execution)
        searchHelper.debounceSearch(query);

        // Force execution of the handler (since debounce uses Handler with delay)
        Robolectric.flushForegroundThreadScheduler();

        // Assert: Ensure `handleSearching()` was indirectly executed
        verify(mockSearchManager).searchCities(eq(query), any(ISearchManager.SearchCallback.class));
    }
    @Test
    public void testHandleCitySelection() {
        // Arrange
        when(mockCursor.getInt(anyInt())).thenReturn(1);
        when(mockCursor.getString(anyInt())).thenReturn("New York, US");

        // Act
        searchHelper.handleCitySelection(mockCursor);

        // Assert
        verify(mockHomePage).forecastDetails(anyInt(), anyString(), anyString(), anyInt());
    }

    @Test
    public void testCleanUp() {
        // Act
        searchHelper.cleanUp();

        // Assert
        verify(mockCityCursorAdapter).changeCursor(null);
    }
}

