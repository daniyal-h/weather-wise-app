package com.daniyalh.WeatherWiseApp.presentation.home;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.daniyalh.WeatherWiseApp.logic.weather.FavouritesManager;
import com.daniyalh.WeatherWiseApp.logic.weather.IFavouritesManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.robolectric.RobolectricTestRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class FavouritesCoordinatorTest {
    @Mock
    private HomePage mockHomePage;
    @Mock
    private FavouritesManager mockFavouritesManager;

    private FavouritesCoordinator favouritesCoordinator;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inject mock FavouritesManager
        favouritesCoordinator = new FavouritesCoordinator(mockHomePage);

        // Use reflection to replace the real FavouritesManager with the mock
        ReflectionTestUtils.setField(favouritesCoordinator, "favouritesManager", mockFavouritesManager);
    }

    @Test
    public void testDisplayFavourites_Success() {
        System.out.println("Starting testDisplayFavourites_Success...");

        // Arrange
        List<String> mockFavourites = Arrays.asList("New York, US", "Tokyo, JP");
        doAnswer(invocation -> {
            IFavouritesManager.FavouritesCallback callback = invocation.getArgument(0);
            callback.onFavouritesFetched(mockFavourites);
            return null;
        }).when(mockFavouritesManager).getFavourites(any());

        // Act
        favouritesCoordinator.displayFavourites();

        // Assert
        verify(mockFavouritesManager).getFavourites(any());
        verify(mockHomePage).setFavouritesAdapter(any(FavouritesAdapter.class));

        System.out.println("Finished testDisplayFavourites_Success.\n");
    }

    @Test
    public void testDisplayFavourites_Error() {
        System.out.println("Starting testDisplayFavourites_Error...");

        // Arrange
        doAnswer(invocation -> {
            IFavouritesManager.FavouritesCallback callback = invocation.getArgument(0);
            callback.onError(new Exception("Database Error"));
            return null;
        }).when(mockFavouritesManager).getFavourites(any());

        // Act
        favouritesCoordinator.displayFavourites();

        // Assert
        verify(mockFavouritesManager).getFavourites(any());
        verify(mockHomePage).showToast(eq("Error fetching favourites"), anyInt());

        System.out.println("Finished testDisplayFavourites_Error.\n");
        System.out.println("----- Finished FavouritesCoordinatorTest -----\n");
    }

    @Test
    public void testClearFavourites_Success() {
        System.out.println("----- Starting FavouritesCoordinatorTest -----\n");
        System.out.println("Starting testClearFavourites_Success...");

        // Arrange
        doAnswer(invocation -> {
            IFavouritesManager.ClearFavouritesCallback callback = invocation.getArgument(0);
            callback.onClearSuccess();
            return null;
        }).when(mockFavouritesManager).clearFavourites(any());

        // Act
        favouritesCoordinator.clearFavourites();

        // Assert
        verify(mockFavouritesManager).clearFavourites(any());
        verify(mockHomePage).showToast(eq("Favourites cleared successfully"), anyInt());

        System.out.println("Finished testClearFavourites_Success.\n");
    }

    @Test
    public void testClearFavourites_Error() {
        System.out.println("Starting testClearFavourites_Error...");

        // Arrange
        doAnswer(invocation -> {
            IFavouritesManager.ClearFavouritesCallback callback = invocation.getArgument(0);
            callback.onClearFailure(new Exception("Database Error"));
            return null;
        }).when(mockFavouritesManager).clearFavourites(any());

        // Act
        favouritesCoordinator.clearFavourites();

        // Assert
        verify(mockFavouritesManager).clearFavourites(any());
        verify(mockHomePage).showToast(eq("Error fetching favourites"), anyInt());

        System.out.println("Finished testClearFavourites_Error.\n");
    }

    @Test
    public void testCleanUp() {
        System.out.println("Starting testCleanUp...");

        // Act
        favouritesCoordinator.cleanUp();

        // Assert
        verify(mockFavouritesManager).shutdown();

        System.out.println("Finished testCleanUp.\n");
    }
}