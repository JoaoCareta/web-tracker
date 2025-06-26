package com.joao.otavio.map_presentation.state

import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.MutableStateFlow

data class WebTrackerMapState(
    val mapZoom: MutableStateFlow<Double> = MutableStateFlow(INITIAL_ZOOM),
    val lastKnowLocation: MutableStateFlow<Point?> = MutableStateFlow(null)
) {
    companion object {
        const val INITIAL_ZOOM = 14.0
    }
}
