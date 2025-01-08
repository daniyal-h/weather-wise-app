package com.daniyalh.WeatherWiseApp;

import com.daniyalh.WeatherWiseApp.logic.*;
import com.daniyalh.WeatherWiseApp.objects.*;
import com.daniyalh.WeatherWiseApp.presentation.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CityTest.class,
        WeatherManagerTest.class,
        WeatherJsonAdapterTest.class,
        SearchManagerTest.class,
        FavouritesManagerTest.class,
        CityCursorAdapterTest.class,
        FavouritesAdapterTest.class
})

public class AllUnitTests {}