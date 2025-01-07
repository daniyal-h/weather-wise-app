package WeatherWiseApp.data;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class AssetListingTest {

    private static final String TAG = "AssetListingTest";

    @Test
    public void listAllTestAssets() throws IOException {
        // Obtain the test context (accesses src/androidTest/assets/)
        Context testContext = InstrumentationRegistry.getInstrumentation().getContext();
        assertNotNull("Test Context should not be null", testContext);

        // List all assets in the test assets directory
        String[] assetFiles = testContext.getAssets().list("");
        assertNotNull("Assets should not be null", assetFiles);

        // Log the assets for debugging
        Log.d(TAG, "Listing all test assets:");
        for (String asset : assetFiles) {
            Log.d(TAG, asset);
        }

        // Check if the stub database exists in test assets
        boolean stubExists = false;
        for (String asset : assetFiles) {
            if (asset.equals("WeatherWiseApp_stub1.db")) {
                stubExists = true;
                break;
            }
        }

        // Assert that the stub database exists in test assets
        assertTrue("WeatherWiseApp_stub1.db should exist in androidTest/assets/", stubExists);
    }

    @Test
    public void listAllMainAssets() throws IOException {
        // Obtain the target context (accesses src/main/assets/)
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertNotNull("Target Context should not be null", targetContext);

        // List all assets in the main assets directory
        String[] assetFiles = targetContext.getAssets().list("");
        assertNotNull("Assets should not be null", assetFiles);

        // Log the assets for debugging
        Log.d(TAG, "Listing all main assets:");
        for (String asset : assetFiles) {
            Log.d(TAG, asset);
        }

        // Check if the stub database exists in main assets
        boolean stubExists = false;
        for (String asset : assetFiles) {
            if (asset.equals("WeatherWiseApp_stub1.db")) {
                stubExists = true;
                break;
            }
        }

        // Assert that the stub database exists in main assets (if applicable)
        assertTrue("WeatherWiseApp_stub1.db should exist in main/assets/", stubExists);
    }
}
