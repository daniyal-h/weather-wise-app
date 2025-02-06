package com.daniyalh.WeatherWiseApp.presentation.home;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.database.Cursor;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.daniyalh.WeatherWiseApp.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HomeUIManagerTest {
    @Mock
    private HomePage mockHomePage;
    @Mock
    private AutoCompleteTextView mockAutoCompleteCityTextView;
    @Mock
    private RecyclerView mockFavouritesRecyclerView;
    @Mock
    private Button mockClearFavouritesButton;
    @Mock
    private Cursor mockCursor;
    @Mock
    private FavouritesAdapter mockFavouritesAdapter;

    private HomeUIManager homeUIManager;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock findViewById() behavior
        when(mockHomePage.findViewById(R.id.autocomplete_city_text_view)).thenReturn(mockAutoCompleteCityTextView);
        when(mockHomePage.findViewById(R.id.favourites_recycler_view)).thenReturn(mockFavouritesRecyclerView);
        when(mockHomePage.findViewById(R.id.back_home_button)).thenReturn(mockClearFavouritesButton);

        // Mock getting a CityCursorAdapter
        when(mockHomePage.getCityCursorAdapter()).thenReturn(Mockito.mock(CityCursorAdapter.class));

        // Initialize HomeUIManager with the mock HomePage
        homeUIManager = new HomeUIManager(mockHomePage);
    }

    @Test
    public void testSetFavouritesAdapter() {
        System.out.println("Starting testSetFavouritesAdapter...");

        // Act
        homeUIManager.setFavouritesAdapter(mockFavouritesAdapter);

        // Assert
        verify(mockFavouritesRecyclerView).setAdapter(mockFavouritesAdapter);

        System.out.println("Finished testSetFavouritesAdapter.\n");
    }

    @Test
    public void testMinimizeAutocomplete() {
        System.out.println("----- Starting HomeUIManagerTest -----\n");
        System.out.println("Starting testMinimizeAutocomplete...");

        // Act
        homeUIManager.minimizeAutocomplete();

        // Assert
        verify(mockAutoCompleteCityTextView).setText("");
        verify(mockAutoCompleteCityTextView).clearFocus();

        System.out.println("Finished testMinimizeAutocomplete.\n");
    }

    @Test
    public void testAutoCompleteCityTextView_TextChange_TriggersDebounceSearch() {
        System.out.println("Starting testAutoCompleteCityTextView_TextChange_TriggersDebounceSearch...");

        // Arrange
        TextWatcher textWatcher = captureTextWatcher();
        CharSequence query = "New York";

        // Act - Simulate user typing
        textWatcher.onTextChanged(query, 0, 0, query.length());

        // Assert
        verify(mockHomePage).debounceSearch(eq(query.toString()));

        System.out.println("Finished testAutoCompleteCityTextView_TextChange_TriggersDebounceSearch.\n");
    }

    @Test
    public void testAutoCompleteCityTextView_ItemSelection_TriggersCitySelection() {
        System.out.println("Starting testAutoCompleteCityTextView_ItemSelection_TriggersCitySelection...");

        // Arrange
        AdapterView<?> mockAdapterView = Mockito.mock(AdapterView.class);
        when(mockAdapterView.getItemAtPosition(anyInt())).thenReturn(mockCursor);

        // Capture the assigned listener
        ArgumentCaptor<AdapterView.OnItemClickListener> captor = ArgumentCaptor.forClass(AdapterView.OnItemClickListener.class);
        verify(mockAutoCompleteCityTextView).setOnItemClickListener(captor.capture());

        // Get the actual assigned listener
        AdapterView.OnItemClickListener onItemClickListener = captor.getValue();
        assertNotNull("OnItemClickListener should be assigned", onItemClickListener);

        // Act - Simulate item selection
        onItemClickListener.onItemClick(mockAdapterView, null, 0, 0);

        // Assert
        verify(mockHomePage).handleCitySelection(any(Cursor.class));

        System.out.println("Finished testAutoCompleteCityTextView_ItemSelection_TriggersCitySelection.\n");
        System.out.println("----- Finished HomeUIManagerTest -----\n");
    }

    // Helper method to capture TextWatcher
    private TextWatcher captureTextWatcher() {
        ArgumentCaptor<TextWatcher> captor = ArgumentCaptor.forClass(TextWatcher.class);
        verify(mockAutoCompleteCityTextView).addTextChangedListener(captor.capture());
        return captor.getValue();
    }
}