package com.daniyalh.WeatherWiseApp.data;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class AssetAccessTest {

    private static final String TAG = "AssetListingTest";

    @Test
    public void listAllAssets() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        try {
            String[] assetFiles = context.getAssets().list("");
            if (assetFiles != null && assetFiles.length > 0) {
                Log.d(TAG, "Listing all assets:");
                for (String fileName : assetFiles) {
                    Log.d(TAG, fileName);
                }
            } else {
                Log.d(TAG, "No assets found.");
            }
        } catch (IOException e) {
            Log.e(TAG, "Error accessing assets: " + e.getMessage());
        }
    }
}