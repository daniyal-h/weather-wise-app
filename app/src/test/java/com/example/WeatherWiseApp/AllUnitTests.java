package com.example.WeatherWiseApp;

import com.example.WeatherWiseApp.logic.WeatherJsonAdapterTest;
import com.example.WeatherWiseApp.objects.CityTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CityTest.class,
        WeatherJsonAdapterTest.class
})

public class AllUnitTests {
}
