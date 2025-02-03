package com.daniyalh.WeatherWiseApp;

import com.daniyalh.WeatherWiseApp.logic.home.SearchManagerTest;
import com.daniyalh.WeatherWiseApp.logic.weather.FavouritesManagerTest;
import com.daniyalh.WeatherWiseApp.logic.weather.WeatherJsonAdapterTest;
import com.daniyalh.WeatherWiseApp.logic.weather.WeatherManagerTest;
import com.daniyalh.WeatherWiseApp.objects.*;
import com.daniyalh.WeatherWiseApp.presentation.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CityTest.class,
        WeatherTest.class,
        ForecastTest.class,
        WeatherManagerTest.class,
        WeatherJsonAdapterTest.class,
        SearchManagerTest.class,
        FavouritesManagerTest.class,
        CityCursorAdapterTest.class,
        FavouritesAdapterTest.class
})

public class AllUnitTests {}