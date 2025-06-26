package com.joao.otavio.map_presentation.viewmodel

import com.joao.otavio.authentication_presentation.utils.MainDispatcherRule
import com.joao.otavio.map_presentation.events.MapEvents
import com.mapbox.geojson.Point
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WebTrackerMapViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: WebTrackerMapViewModel

    @Before
    fun setup() {
        viewModel = WebTrackerMapViewModel()
    }

    /*
     * Map Zoom Tests
     */

    @Test
    fun `given valid zoom value, when zoom is changed, then state should be updated`() = runTest {
        // Mockk
        val newZoom = DEFAULT_ZOOM

        // Run Test
        viewModel.onUiEvents(MapEvents.OnMapZoomChanged(newZoom))

        // Assert
        assertEquals(newZoom, viewModel.webTrackerMapState.mapZoom.value)
    }

    @Test
    fun `given null zoom value, when zoom is changed, then state should remain unchanged`() = runTest {
        // Mockk
        val initialZoom = viewModel.webTrackerMapState.mapZoom.value

        // Run Test
        viewModel.onUiEvents(MapEvents.OnMapZoomChanged(null))

        // Assert
        assertEquals(initialZoom, viewModel.webTrackerMapState.mapZoom.value)
    }

    /*
     * Location Update Tests
     */

    @Test
    fun `given valid location, when location is updated, then state should reflect new location`() = runTest {
        // Mockk
        val newLocation = Point.fromLngLat(LONGITUDE, LATITUDE)

        // Run Test
        viewModel.onUiEvents(MapEvents.OnNewLocation(newLocation))

        // Assert
        assertEquals(newLocation, viewModel.webTrackerMapState.lastKnowLocation.value)
    }

    @Test
    fun `given null location, when location is updated, then state should reflect null location`() = runTest {
        // Run Test
        viewModel.onUiEvents(MapEvents.OnNewLocation(null))

        // Assert
        assertNull(viewModel.webTrackerMapState.lastKnowLocation.value)
    }

    @Test
    fun `given multiple location updates, when locations change, then state should reflect most recent location`() = runTest {
        // Mockk
        val firstLocation = Point.fromLngLat(LONGITUDE, LATITUDE)
        val secondLocation = Point.fromLngLat(LONGITUDE_2, LATITUDE_2)

        // Run Test
        viewModel.onUiEvents(MapEvents.OnNewLocation(firstLocation))
        viewModel.onUiEvents(MapEvents.OnNewLocation(secondLocation))

        // Assert
        assertEquals(secondLocation, viewModel.webTrackerMapState.lastKnowLocation.value)
    }

    /*
     * Multiple Events Tests
     */

    @Test
    fun `given zoom and location updates, when both change, then state should reflect both changes`() = runTest {
        // Mockk
        val newLocation = Point.fromLngLat(LONGITUDE, LATITUDE)
        val newZoom = DEFAULT_ZOOM

        // Run Test
        viewModel.onUiEvents(MapEvents.OnNewLocation(newLocation))
        viewModel.onUiEvents(MapEvents.OnMapZoomChanged(newZoom))

        // Assert
        assertEquals(newLocation, viewModel.webTrackerMapState.lastKnowLocation.value)
        assertEquals(newZoom, viewModel.webTrackerMapState.mapZoom.value)
    }

    @Test
    fun `given rapid zoom changes, when zoom updates multiple times, then state should reflect final zoom`() = runTest {
        // Mockk
        val finalZoom = DEFAULT_ZOOM + 2.0

        // Run Test
        viewModel.onUiEvents(MapEvents.OnMapZoomChanged(DEFAULT_ZOOM))
        viewModel.onUiEvents(MapEvents.OnMapZoomChanged(DEFAULT_ZOOM + 1.0))
        viewModel.onUiEvents(MapEvents.OnMapZoomChanged(finalZoom))

        // Assert
        assertEquals(finalZoom, viewModel.webTrackerMapState.mapZoom.value)
    }

    /*
     * Initial State Tests
     */

    @Test
    fun `given new viewmodel instance, when checking initial state, then should have default values`() = runTest {
        // Assert
        assertNull(viewModel.webTrackerMapState.lastKnowLocation.value)
        assertEquals(DEFAULT_ZOOM, viewModel.webTrackerMapState.mapZoom.value)
    }

    companion object {
        const val DEFAULT_ZOOM = 14.0
        const val LONGITUDE = -46.633308
        const val LATITUDE = -23.550520
        const val LONGITUDE_2 = -46.633400
        const val LATITUDE_2 = -23.550600
    }
}
