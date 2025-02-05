package com.daniyalh.WeatherWiseApp.presentation.home;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daniyalh.WeatherWiseApp.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowDrawable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(RobolectricTestRunner.class)
public class FavouritesAdapterTest {

    private Context context;
    private List<String> favouriteCities;
    private FavouritesAdapter.OnItemClickListener mockListener;
    private FavouritesAdapter adapter;

    @Before
    public void setUp() {
        context = Robolectric.buildActivity(android.app.Activity.class).create().get();
        favouriteCities = Arrays.asList("City1, Country1", "City2, Country2", "City3, Country3");
        mockListener = mock(FavouritesAdapter.OnItemClickListener.class);
        adapter = new FavouritesAdapter(context, favouriteCities, mockListener);
    }

    @Test
    public void testGetItemCount() {
        System.out.println("----- Starting FavouritesAdapterTest -----\n");
        System.out.println("Starting testGetItemCount...");
        assertEquals("Item count should match list size", favouriteCities.size(), adapter.getItemCount());
        System.out.println("Finished testGetItemCount.\n");
    }

    @Test
    public void testOnBindViewHolderAndClick() {
        System.out.println("Starting testOnBindViewHolderAndClick...");

        // Use a proper ViewGroup parent for inflating the view holder
        ViewGroup parent = new LinearLayout(context);

        // Create a view holder using the adapter
        FavouritesAdapter.ViewHolder viewHolder = (FavouritesAdapter.ViewHolder) adapter.onCreateViewHolder(parent, 0);

        // Bind the view holder to the first item
        adapter.onBindViewHolder(viewHolder, 0);

        // Check that the TextView was set with the correct city name
        TextView textView = viewHolder.itemView.findViewById(R.id.favourite_text_view);
        assertEquals("City1, Country1", textView.getText().toString());

        // Verify that a drawable was set using Robolectric
        Drawable[] drawables = textView.getCompoundDrawables();
        assertNotNull("Drawable should be set for the left icon", drawables[0]); // Left drawable
        assertNull("No top drawable expected", drawables[1]);
        assertNull("No right drawable expected", drawables[2]);
        assertNull("No bottom drawable expected", drawables[3]);

        // Verify that the drawable is from the expected set
        List<Integer> iconList = Arrays.stream(adapter.icons).boxed().collect(Collectors.toList());
        ShadowDrawable shadowDrawable = Shadow.extract(drawables[0]);
        int drawableResId = shadowDrawable.getCreatedFromResId();
        assertTrue("Drawable should be from the predefined set", iconList.contains(drawableResId));

        // Simulate a click on the item view
        viewHolder.itemView.performClick();

        // Verify that the click listener was invoked with the correct city
        verify(mockListener).onItemClick("City1, Country1");

        System.out.println("Finished testOnBindViewHolderAndClick.\n");
        System.out.println("----- Finished FavouritesAdapterTest -----");
    }
}
