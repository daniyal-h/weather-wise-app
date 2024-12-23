package com.example.WeatherWiseApp;

import com.example.WeatherWiseApp.logic.WeatherJsonAdapterTest;
import com.example.WeatherWiseApp.logic.WeatherManagerTest;
import com.example.WeatherWiseApp.objects.CityTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CityTest.class,
        WeatherManagerTest.class,
        WeatherJsonAdapterTest.class
})

public class AllUnitTests {
}
