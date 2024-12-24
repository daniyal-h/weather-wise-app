package com.daniyalh.WeatherWiseApp;

import com.daniyalh.WeatherWiseApp.logic.CityManagerTest;
import com.daniyalh.WeatherWiseApp.logic.WeatherJsonAdapterTest;
import com.daniyalh.WeatherWiseApp.logic.WeatherManagerTest;
import com.daniyalh.WeatherWiseApp.objects.CityTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CityTest.class,
        CityManagerTest.class,
        WeatherManagerTest.class,
        WeatherJsonAdapterTest.class
})

public class AllUnitTests {
}
