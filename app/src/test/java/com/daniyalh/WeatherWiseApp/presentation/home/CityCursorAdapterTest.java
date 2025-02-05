package com.daniyalh.WeatherWiseApp.presentation.home;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import android.content.Context;
import android.database.Cursor;

import com.daniyalh.WeatherWiseApp.presentation.UIConstants;
import com.daniyalh.WeatherWiseApp.presentation.home.CityCursorAdapter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class CityCursorAdapterTest {

    private Context context;
    private Cursor dummyCursor;

    @Before
    public void setUp() {
        // Use Robolectric to obtain an application context.
        context = org.robolectric.RuntimeEnvironment.getApplication();
        // Create a dummy cursor using Mockito.
        dummyCursor = mock(Cursor.class);
    }

    @Test
    public void testConvertToStringReturnsSelectionFlag() {
        System.out.println("----- Starting CityCursorAdapterTest -----\n");

        // Initialize the adapter with a dummy cursor.
        CityCursorAdapter adapter = new CityCursorAdapter(context, dummyCursor);
        // Invoke convertToString and verify that it returns the expected constant.
        CharSequence result = adapter.convertToString(dummyCursor);
        Assert.assertEquals(UIConstants.SELECTION_FLAG, result);

        System.out.println("----- Finished CityCursorAdapterTest -----\n");
    }
}
