package WeatherWiseApp.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.daniyalh.WeatherWiseApp.data.MyDatabaseHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class MyDatabaseHelperInstrumentedTest {

    private MyDatabaseHelper dbHelper;
    private Context context;
    private File dbFile;
    private final Map<String, String[]> favouriteCities = new HashMap<>();

    @Before
    public void setUp() throws IOException {
        // Obtain the target context (app under test)
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Define the database name
        String dbName = "WeatherWiseApp_stub1.db";

        // Get the database file path
        dbFile = context.getDatabasePath(dbName);

        // Ensure the parent directories exist
        File parentDir = dbFile.getParentFile();
        if (!parentDir.exists()) {
            boolean dirsCreated = parentDir.mkdirs();
            Log.d("MyDatabaseHelperTest", "Database directory created: " + dirsCreated);
            assertTrue("Failed to create database directory", dirsCreated);
        } else {
            Log.d("MyDatabaseHelperTest", "Database directory exists.");
        }

        // Delete existing database file if it exists
        if (dbFile.exists()) {
            boolean deleted = dbFile.delete();
            Log.d("MyDatabaseHelperTest", "Existing DB File Deleted: " + deleted);
        }

        // Copy the stub database from test assets to the database path
        try (InputStream is = context.getAssets().open(dbName);
             OutputStream os = new FileOutputStream(dbFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            Log.d("MyDatabaseHelperTest", "Stub database copied to database path.");
        } catch (IOException e) {
            Log.e("MyDatabaseHelperTest", "Error copying stub database: " + e.getMessage());
            throw e; // Re-throw to fail the setup
        }

        // Initialize the MyDatabaseHelper with the database name
        dbHelper = MyDatabaseHelper.getInstance(context, dbName);

        // Ensure the database is opened
        try {
            dbHelper.getWritableDatabase();
            Log.d("MyDatabaseHelperTest", "Database opened successfully.");
        } catch (Exception e) {
            Log.e("MyDatabaseHelperTest", "Error opening database: " + e.getMessage());
            throw e; // Re-throw to fail the setup
        }
    }

    @After
    public void tearDown() {
        // Close the database helper to release resources
        if (dbHelper != null) {
            dbHelper.close();
        }

        // Delete the temporary database file
        if (dbFile != null && dbFile.exists()) {
            boolean deleted = dbFile.delete();
            Log.d("MyDatabaseHelperTest", "Temporary DB File Deleted: " + deleted);
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
        assertFalse("Favourites should be cleared", cursor.moveToFirst());
        cursor.close();

        dbHelper.updateFavouriteStatus(1, true);
        dbHelper.updateFavouriteStatus(2, true);

        cursor = dbHelper.getFavouriteCities();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // get all necessary parameters
                int cityID = cursor.getInt(cursor.getColumnIndexOrThrow("cityID"));
                String displayName = cursor.getString(cursor.getColumnIndexOrThrow("display_name"));
                String country = cursor.getString(cursor.getColumnIndexOrThrow("country"));
                favouriteCities.put(displayName, new String[]{String.valueOf(cityID), country});
            } while (cursor.moveToNext());
        }

        assertEquals(2, favouriteCities.size());
        cursor.close();
    }

    @Test
    public void testDatabasePathInitialization() {
        assertNotNull("Context should not be null", context);
        File dbPath = context.getDatabasePath("WeatherWiseApp_stub1.db");
        assertNotNull("getDatabasePath should return a File", dbPath);
        assertEquals(dbFile, dbPath);
    }
}