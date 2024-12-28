package com.daniyalh.WeatherWiseApp.presentation;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

public class CityCursorAdapter extends SimpleCursorAdapter {
    public CityCursorAdapter(Context context, Cursor cursor) {
        super(context, android.R.layout.simple_dropdown_item_1line, cursor,
                new String[]{"display_name"},
                new int[]{android.R.id.text1},0);
    }
    @Override
    public CharSequence convertToString(Cursor cursor) {
        return "inSelection";
    }
}

