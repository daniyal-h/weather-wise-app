package WeatherWiseApp.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class MyDatabaseHelperInstrumentedTest {

    private MyDatabaseHelper dbHelper;
    private Context context;
    private File testDbFile;

    @Before
    public void setUp() {
        // Obtain the test context (to access test-specific assets)
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertNotNull("Context should not be null", context);

        dbHelper = MyDatabaseHelper.getInstance(context, "WeatherWiseApp_stub1.db");

        // Ensure the database is opened
        try {
            dbHelper.getWritableDatabase();
            Log.d("MyDatabaseHelperTest", "Test Database opened successfully.");
        } catch (Exception e) {
            Log.e("MyDatabaseHelperTest", "Error opening test database: " + e.getMessage());
            fail("Failed to open test database.");
        }
    }

    @After
    public void tearDown() {
        // Close the database helper to release resources
        if (dbHelper != null) {
            dbHelper.close();
        }

        // Delete the temporary database file
        if (testDbFile != null && testDbFile.exists()) {
            boolean deleted = testDbFile.delete();
            Log.d("MyDatabaseHelperTest", "Temporary DB File Deleted: " + deleted);
        }
    }

    @Test
    public void testFavouriting() {
        dbHelper.logAllDatabases(context);
        dbHelper.clearFavourites(); // reset
        dbHelper.logAllDatabases(context);

        Cursor cursor = dbHelper.getFavouriteCities();
        assertFalse("Favourites should be cleared", cursor.moveToFirst());
        cursor.close();

        // Insert test data
        dbHelper.updateFavouriteStatus(1, true);
        dbHelper.updateFavouriteStatus(2, true);

        // Clear favourites
        dbHelper.clearFavourites();
        dbHelper.logAllDatabases(context);

        // Verify favourites are cleared
        cursor = dbHelper.getFavouriteCities();
        dbHelper.logAllDatabases(context);
        assertFalse("Favourites should be cleared", cursor.moveToFirst());
        cursor.close();

        dbHelper.updateFavouriteStatus(1, true);
        dbHelper.updateFavouriteStatus(82, true);
        dbHelper.logAllDatabases(context);

        String[] expectedCity1 = new String[]{"New York City, US", "United States"};
        String[] expectedCity2 = new String[]{"Winnipeg, CA", "Canada"};

        cursor = dbHelper.getFavouriteCities();
        dbHelper.logAllDatabases(context);
        Map<Integer, String[]> favouriteCities = new HashMap<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // get all necessary parameters
                int cityID = cursor.getInt(cursor.getColumnIndexOrThrow("cityID"));
                String displayName = cursor.getString(cursor.getColumnIndexOrThrow("display_name"));
                String country = cursor.getString(cursor.getColumnIndexOrThrow("country"));
                favouriteCities.put(cityID, new String[]{displayName, country});
            } while (cursor.moveToNext());
        }

        assertEquals(2, favouriteCities.size());

        assertArrayEquals(expectedCity1, favouriteCities.get(1));
        assertArrayEquals(expectedCity2, favouriteCities.get(82));

        cursor.close();
        dbHelper.clearFavourites(); // reset
        dbHelper.logAllDatabases(context);
    }

    @Test
    public void testGetCitiesByQuery() {
        dbHelper.logAllDatabases(context);
        dbHelper.clearFavourites();
        dbHelper.updateFavouriteStatus(89, true);
        dbHelper.logAllDatabases(context);
        Map<Integer, Object[]> cities = new HashMap<>();

        Object[] expectedCity1 = new Object[]{"CA", "Winnipeg, Canada", 0};
        Object[] expectedCity2 = new Object[]{"CA", "Windsor, Canada", 1};

        Cursor cursor = dbHelper.getCitiesByQuery("win");
        assertEquals(2, cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                int cityID = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String countryCode = cursor.getString(cursor.getColumnIndexOrThrow("country_code"));
                String displayName = cursor.getString(cursor.getColumnIndexOrThrow("display_name"));
                int isFavourite = cursor.getInt(cursor.getColumnIndexOrThrow("is_favourite"));

                cities.put(cityID, new Object[]{countryCode, displayName, isFavourite});
            } while (cursor.moveToNext());
        }
        cursor.close();

        assertEquals(2, cities.size());

        assertArrayEquals(expectedCity1, cities.get(82));
        assertArrayEquals(expectedCity2, cities.get(89));

        dbHelper.logAllDatabases(context);

        // edge case: empty space
        cursor = dbHelper.getCitiesByQuery(" ");
        assertEquals(0, cursor.getCount());
        cursor.close();

        // edge case: many empty space
        cursor = dbHelper.getCitiesByQuery("                       ");
        assertEquals(0, cursor.getCount());
        cursor.close();

        // edge case: no input (logic layer would account for this but since we test only data, it returns the limit)
        cursor = dbHelper.getCitiesByQuery("");
        assertEquals(10, cursor.getCount());
        cursor.close();

        dbHelper.logAllDatabases(context);
    }
}