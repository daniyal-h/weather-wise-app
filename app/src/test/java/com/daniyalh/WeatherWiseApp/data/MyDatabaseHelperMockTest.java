package com.daniyalh.WeatherWiseApp.data;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class MyDatabaseHelperMockTest {

    @Mock
    Context mockContext;

    @Mock
    AssetManager mockAssetManager;

    private MyDatabaseHelper dbHelper;
    private File tempDbFile;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        // Mock the Context's AssetManager
        when(mockContext.getAssets()).thenReturn(mockAssetManager);

        // When AssetManager.open is called with "WeatherWiseApp_stub1.db", return the stub InputStream
        InputStream stubDbInputStream = new FileInputStream(new File("src/test/resources/WeatherWiseApp_stub1.db"));
        when(mockAssetManager.open("WeatherWiseApp_stub1.db")).thenReturn(stubDbInputStream);

        // Create a temporary file to act as the database path
        tempDbFile = File.createTempFile("WeatherWiseApp_stub1", ".db");
        when(mockContext.getDatabasePath("WeatherWiseApp_stub1.db")).thenReturn(tempDbFile);

        // Initialize the MyDatabaseHelper with the mocked Context and stub database name
        dbHelper = MyDatabaseHelper.getInstance(mockContext, "WeatherWiseApp_stub1.db");
    }

    @After
    public void tearDown() {
        // Close the database helper to release resources
        if (dbHelper != null) {
            dbHelper.close();
        }

        // Delete the temporary database file
        if (tempDbFile != null && tempDbFile.exists()) {
            tempDbFile.delete();
        }
    }

    @Test
    public void testClearFavourites() {
        // Insert test data
        dbHelper.updateFavouriteStatus(1, true);
        dbHelper.updateFavouriteStatus(2, true);

        // Clear favourites
        dbHelper.clearFavourites();

        // Verify favourites are cleared
        Cursor cursor = dbHelper.getFavouriteCities();
        assertFalse(cursor.moveToFirst()); // No favourites should be present
        cursor.close();
    }
}

