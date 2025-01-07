package com.daniyalh.WeatherWiseApp;

import com.daniyalh.WeatherWiseApp.logic.*;
import com.daniyalh.WeatherWiseApp.objects.CityTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CityTest.class,
        CityManagerTest.class,
        WeatherManagerTest.class,
        WeatherJsonAdapterTest.class,
        SearchManagerTest.class
})

public class AllUnitTests {
}
