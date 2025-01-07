package com.daniyalh.WeatherWiseApp.logic;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;

import com.daniyalh.WeatherWiseApp.data.MyDatabaseHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.junit.runner.RunWith;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class FavouritesManagerTest {

    private MyDatabaseHelper mockDbHelper;
    private FavouritesManager favouritesManager;

    @Before
    public void setUp() {
        // Create a synchronous executor that executes tasks immediately
        ExecutorService immediateExecutor = new AbstractExecutorService() {
            private volatile boolean terminated = false;

            @Override
            public void shutdown() {
                terminated = true;
            }

            @Override
            public List<Runnable> shutdownNow() {
                shutdown();
                return new ArrayList<>();
            }

            @Override
            public boolean isShutdown() {
                return terminated;
            }

            @Override
            public boolean isTerminated() {
                return terminated;
            }

            @Override
            public boolean awaitTermination(long timeout, TimeUnit unit) {
                return terminated;
            }

            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };

        Handler mainHandler = new Handler(Looper.getMainLooper());

        mockDbHelper = Mockito.mock(MyDatabaseHelper.class);
        favouritesManager = FavouritesManager.getInstance(mockDbHelper);

        // Inject synchronous executor and handler.
        favouritesManager.setAsynchronicity(immediateExecutor, mainHandler);
    }

    @Test
    public void testFavouritesFlow() {
        // Step 1: Clear all favourites.
        IFavouritesManager.ClearFavouritesCallback clearCallback = Mockito.mock(IFavouritesManager.ClearFavouritesCallback.class);
        favouritesManager.clearFavourites(clearCallback);
        Robolectric.flushForegroundThreadScheduler();
        verify(mockDbHelper).clearFavourites();
        verify(clearCallback).onClearSuccess();

        // Step 2: Set 5 favourites.
        int[] cityIDs = {1, 2, 3, 4, 5};
        for (int id : cityIDs) {
            favouritesManager.toggleFavourite(id, true);
            verify(mockDbHelper).updateFavouriteStatus(id, true);
        }

        // Step 3: Remove 1 favourite
        int removeCityID = 3;
        favouritesManager.toggleFavourite(removeCityID, false);
        verify(mockDbHelper).updateFavouriteStatus(removeCityID, false);

        // Step 4: Prepare a dummy Cursor simulating 4 remaining favourites
        Cursor dummyCursor = Mockito.mock(Cursor.class);
        // Setup cursor to simulate 4 rows
        Mockito.when(dummyCursor.moveToFirst()).thenReturn(true);
        // Return true for the first 3 calls to moveToNext(), then false
        Mockito.when(dummyCursor.moveToNext()).thenReturn(true, true, true, false);
        Mockito.when(dummyCursor.getColumnIndexOrThrow("cityID")).thenReturn(0);
        Mockito.when(dummyCursor.getColumnIndexOrThrow("display_name")).thenReturn(1);
        Mockito.when(dummyCursor.getColumnIndexOrThrow("country")).thenReturn(2);
        // Simulate data for 4 rows
        Mockito.when(dummyCursor.getInt(0)).thenReturn(1, 2, 4, 5);
        Mockito.when(dummyCursor.getString(1))
                .thenReturn("City1, CC", "City2, CC", "City4, CC", "City5, CC");
        Mockito.when(dummyCursor.getString(2))
                .thenReturn("CountryName", "CountryName", "CountryName", "CountryName");
        Mockito.when(dummyCursor.isClosed()).thenReturn(true);
        Mockito.when(mockDbHelper.getFavouriteCities()).thenReturn(dummyCursor);

        // Step 5: Get all favourites
        IFavouritesManager.FavouritesCallback getCallback = Mockito.mock(IFavouritesManager.FavouritesCallback.class);
        favouritesManager.getFavourites(getCallback);
        Robolectric.flushForegroundThreadScheduler();

        // Capture and verify the list of favourites
        ArgumentCaptor<List> listCaptor = ArgumentCaptor.forClass(List.class);
        verify(getCallback).onFavouritesFetched(listCaptor.capture());
        List<String> favourites = listCaptor.getValue();

        // Assert that 4 favourites are returned
        assertEquals(4, favourites.size());

        assertTrue(favourites.contains("City1, CC"));
        assertTrue(favourites.contains("City2, CC"));
        assertTrue(favourites.contains("City4, CC"));
        assertTrue(favourites.contains("City5, CC"));
    }
}
