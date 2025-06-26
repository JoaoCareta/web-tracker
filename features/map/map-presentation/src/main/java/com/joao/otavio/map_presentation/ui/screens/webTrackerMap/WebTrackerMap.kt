package com.joao.otavio.map_presentation.ui.screens.webTrackerMap

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.joao.otavio.core.navigation.WebTrackerScreens
import com.joao.otavio.core.util.NavigationEvent
import com.joao.otavio.core.util.PermissionUtils
import com.joao.otavio.design_system.buttons.WebTrackerCircularButton
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.dimensions.LocalPaddings
import com.joao.otavio.design_system.dimensions.Paddings
import com.joao.otavio.design_system.scaffold.WebTrackerScaffold
import com.joao.otavio.map_presentation.events.MapEvents
import com.joao.otavio.map_presentation.viewmodel.WebTrackerMapViewModel
import com.joao.otavio.webtracker.common.desygn.system.R
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.animation.MapAnimationOptions.Companion.mapAnimationOptions
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun WebTrackerMap(
    webTrackerMapViewModel: WebTrackerMapViewModel = hiltViewModel(),
    theme: WebTrackerTheme = WebTrackerTheme,
    onNavigate: (NavigationEvent.Navigate) -> Unit,
) {
    val paddingValues = LocalPaddings.current
    val context = LocalContext.current
    val mapViewportState = rememberMapViewportState()

    val mapState = webTrackerMapViewModel.webTrackerMapState
    val lastKnownLocation by mapState.lastKnowLocation.collectAsState()
    val isFollowingPuck = remember { mutableStateOf(true) }
    val currentMapZoom by mapState.mapZoom.collectAsState()

    LaunchedEffect(Unit) {
        if (!PermissionUtils.checkAllPermissionsGranted(context)) {
            onNavigate(WebTrackerScreens.MissingPermissions.navigateKeepingInStack())
        }
    }

    WebTrackerScaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        MapContent(
            mapViewportState = mapViewportState,
            lastKnownLocation = lastKnownLocation,
            isFollowingPuck = isFollowingPuck,
            currentMapZoom = currentMapZoom,
            onLocationUpdate = { newLocation, mapView ->
                webTrackerMapViewModel.onUiEvents(MapEvents.OnNewLocation(newLocation))
                if (isFollowingPuck.value) {
                    mapViewportState.setCameraOptions(CameraOptions.Builder().center(newLocation).build())
                    mapView.gestures.focalPoint = mapView.mapboxMap.pixelForCoordinate(newLocation)
                }
            },
            onMapZoomChanged = { zoom ->
                webTrackerMapViewModel.onUiEvents(MapEvents.OnMapZoomChanged(zoom))
            },
            theme = theme,
            paddingValues = paddingValues
        )
    }
}

@Composable
private fun MapContent(
    mapViewportState: MapViewportState,
    lastKnownLocation: Point?,
    isFollowingPuck: MutableState<Boolean>,
    currentMapZoom: Double,
    onLocationUpdate: (Point, MapView) -> Unit,
    onMapZoomChanged: (Double?) -> Unit,
    theme: WebTrackerTheme,
    paddingValues: Paddings
) {
    Box(modifier = Modifier.fillMaxSize()) {
        OptimizedMapbox(
            mapViewportState = mapViewportState,
            currentMapZoom = currentMapZoom,
            onLocationUpdate = onLocationUpdate
        )

        NavigationControls(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = paddingValues.medium, bottom = paddingValues.xHuge),
            lastKnownLocation = lastKnownLocation,
            isFollowingPuck = isFollowingPuck,
            currentMapZoom = currentMapZoom,
            mapViewportState = mapViewportState,
            theme = theme,
            paddingValues = paddingValues
        )

        ZoomControls(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = paddingValues.medium, bottom = paddingValues.xHuge),
            mapViewportState = mapViewportState,
            onMapZoomChanged = onMapZoomChanged,
            theme = theme,
            paddingValues = paddingValues
        )
    }
}

@Composable
private fun OptimizedMapbox(
    mapViewportState: MapViewportState,
    currentMapZoom: Double,
    onLocationUpdate: (Point, MapView) -> Unit
) {
    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        style = { MapStyle(style = Style.STANDARD) },
        mapViewportState = mapViewportState,
    ) {
        MapEffect(currentMapZoom) { mapView ->
            mapView.mapboxMap.setCamera(
                cameraOptions {
                    zoom(currentMapZoom)
                    pitch(0.0)
                    bearing(0.0)
                }
            )

            mapView.location.apply {
                enabled = true
                puckBearingEnabled = true
                locationPuck = createDefault2DPuck(withBearing = true)
                puckBearing = PuckBearing.COURSE
                pulsingEnabled = true

                addOnIndicatorPositionChangedListener { location ->
                    onLocationUpdate(location, mapView)
                }
            }
        }
    }
}

@Composable
private fun NavigationControls(
    modifier: Modifier,
    lastKnownLocation: Point?,
    isFollowingPuck: MutableState<Boolean>,
    currentMapZoom: Double,
    mapViewportState: MapViewportState,
    theme: WebTrackerTheme,
    paddingValues: Paddings
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(paddingValues.small)
    ) {
        WebTrackerCircularButton(
            icon = R.drawable.ic_gps,
            onClick = {
                lastKnownLocation?.let { location ->
                    mapViewportState.easeTo(
                        CameraOptions.Builder()
                            .center(location)
                            .pitch(0.0)
                            .zoom(currentMapZoom)
                            .build(),
                        mapAnimationOptions {
                            duration(TRANSITION_DURATION)
                        }
                    )
                }
            },
            theme = theme,
        )

        WebTrackerCircularButton(
            icon = R.drawable.ic_location,
            onClick = {
                isFollowingPuck.value = !isFollowingPuck.value
            },
            isActive = isFollowingPuck.value,
            theme = theme
        )
    }
}

@Composable
private fun ZoomControls(
    modifier: Modifier,
    mapViewportState: MapViewportState,
    onMapZoomChanged: (Double?) -> Unit,
    theme: WebTrackerTheme,
    paddingValues: Paddings
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(paddingValues.small)
    ) {
        WebTrackerCircularButton(
            icon = Icons.Default.Add,
            onClick = {
                mapViewportState.setCameraOptions {
                    val newMapZoom = mapViewportState.cameraState?.zoom?.plus(1)
                    onMapZoomChanged(newMapZoom)
                    zoom(newMapZoom)
                }
            },
            theme = theme
        )

        WebTrackerCircularButton(
            icon = R.drawable.ic_minus,
            onClick = {
                mapViewportState.setCameraOptions {
                    val newMapZoom = mapViewportState.cameraState?.zoom?.minus(1)
                    onMapZoomChanged(newMapZoom)
                    zoom(newMapZoom)
                }
            },
            theme = theme
        )
    }
}

@Preview
@Composable
fun WebTrackerMapPreview() {
    WebTrackerMap(
        onNavigate = {}
    )
}
const val TRANSITION_DURATION = 500L
