package com.daniyalh.WeatherWiseApp;

import com.daniyalh.WeatherWiseApp.logic.forecast.ForecastJsonAdapterTest;
import com.daniyalh.WeatherWiseApp.logic.forecast.ForecastManagerTest;
import com.daniyalh.WeatherWiseApp.logic.home.SearchManagerTest;
import com.daniyalh.WeatherWiseApp.logic.weather.FavouritesManagerTest;
import com.daniyalh.WeatherWiseApp.logic.weather.WeatherJsonAdapterTest;
import com.daniyalh.WeatherWiseApp.logic.weather.WeatherManagerTest;
import com.daniyalh.WeatherWiseApp.objects.*;
import com.daniyalh.WeatherWiseApp.presentation.home.CityCursorAdapterTest;
import com.daniyalh.WeatherWiseApp.presentation.home.FavouritesAdapterTest;
import com.daniyalh.WeatherWiseApp.presentation.home.FavouritesCoordinatorTest;
import com.daniyalh.WeatherWiseApp.presentation.home.HomeUIManagerTest;
import com.daniyalh.WeatherWiseApp.presentation.home.SearchHelperTest;

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
        FavouritesAdapterTest.class,
        SearchHelperTest.class,
        HomeUIManagerTest.class,
        FavouritesCoordinatorTest.class,
        ForecastManagerTest.class,
        ForecastJsonAdapterTest.class
})

public class AllUnitTests {}