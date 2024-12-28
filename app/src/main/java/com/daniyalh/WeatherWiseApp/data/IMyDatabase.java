package com.daniyalh.WeatherWiseApp.data;

import android.database.Cursor;

public interface IMyDatabase {
    Cursor getCitiesByQuery(String query);
}
