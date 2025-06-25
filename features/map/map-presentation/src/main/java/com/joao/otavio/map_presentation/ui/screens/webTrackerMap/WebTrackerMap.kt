package com.joao.otavio.map_presentation.ui.screens.webTrackerMap

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.joao.otavio.core.navigation.WebTrackerScreens
import com.joao.otavio.core.util.NavigationEvent
import com.joao.otavio.core.util.PermissionUtils
import com.joao.otavio.design_system.buttons.WebTrackerCircularButton
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.dimensions.LocalPaddings
import com.joao.otavio.design_system.scaffold.WebTrackerScaffold
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
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun WebTrackerMap(
    theme: WebTrackerTheme = WebTrackerTheme,
    onNavigate: (NavigationEvent.Navigate) -> Unit,
) {
    val paddingValues = LocalPaddings.current
    val mapViewportState = rememberMapViewportState()
    val lastKnownLocation = remember { mutableStateOf<Point?>(null) }
    val isFollowingPuck = remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(true) {
        if (!PermissionUtils.checkAllPermissionsGranted(context)) {
            onNavigate(WebTrackerScreens.MissingPermissions.navigateKeepingInStack())
        }
    }

    WebTrackerScaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Mapbox(
                mapViewportState = mapViewportState,
                isFollowingPuck = isFollowingPuck.value
            ) { newLocation, mapView ->
                lastKnownLocation.value = newLocation
                if (isFollowingPuck.value) {
                    mapViewportState.setCameraOptions(CameraOptions.Builder().center(newLocation).build())
                    mapView.gestures.focalPoint = mapView.mapboxMap.pixelForCoordinate(newLocation)
                }
            }

            // Coluna da esquerda
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = paddingValues.medium)
                    .fillMaxHeight(COLUM_WEIGHT),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

            }

            // Coluna da direita
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = paddingValues.medium, bottom = paddingValues.xHuge),
                verticalArrangement = Arrangement.spacedBy(paddingValues.small)
            ) {
                WebTrackerCircularButton(
                    icon = R.drawable.ic_gps,
                    onClick = {
                        lastKnownLocation.value?.let { location ->
                            mapViewportState.easeTo(
                                CameraOptions.Builder()
                                    .center(location)
                                    .zoom(ZOOM)
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
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = paddingValues.medium, bottom = paddingValues.xHuge),
                verticalArrangement = Arrangement.spacedBy(paddingValues.small)
            ) {
                WebTrackerCircularButton(
                    icon = Icons.Default.Add,
                    onClick = {
                        mapViewportState.setCameraOptions {
                            zoom(mapViewportState.cameraState?.zoom?.plus(1))
                        }
                    },
                    theme = theme
                )

                WebTrackerCircularButton(
                    icon = R.drawable.ic_minus,
                    onClick = {
                        mapViewportState.setCameraOptions {
                            zoom(mapViewportState.cameraState?.zoom?.minus(1))
                        }
                    },
                    theme = theme
                )
            }
        }
    }
}

@Composable
fun Mapbox(
    mapViewportState: MapViewportState,
    isFollowingPuck: Boolean,
    onLocationUpdate: (Point, MapView) -> Unit
) {
    MapboxMap(
        modifier = Modifier
            .fillMaxSize(),
        style = { MapStyle(style = Style.STANDARD) },
        mapViewportState = mapViewportState,
    ) {
        MapEffect(isFollowingPuck) { mapView ->

            mapView.mapboxMap.setCamera(
                cameraOptions {
                    zoom(ZOOM)
                    pitch(0.0)
                    bearing(0.0)
                }
            )

            val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener { location ->
                onLocationUpdate(location, mapView)
            }

            mapView.location.updateSettings {
                enabled = true
                puckBearingEnabled = true
                locationPuck = createDefault2DPuck(withBearing = true)
                puckBearing = PuckBearing.COURSE
            }

            mapView.location.apply {
                addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
                pulsingEnabled = true
            }
        }
    }
}

@Preview
@Composable
fun WebTrackerMapPreview() {
    WebTrackerMap(
        onNavigate = {}
    )
}

const val ZOOM = 14.0
const val COLUM_WEIGHT = 0.5f
const val TRANSITION_DURATION = 500L
