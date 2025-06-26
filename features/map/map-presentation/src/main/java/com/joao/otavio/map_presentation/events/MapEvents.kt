package com.joao.otavio.map_presentation.events

import com.mapbox.geojson.Point

sealed class MapEvents {
    data class OnMapZoomChanged(val newZoomValue: Double?) : MapEvents()
    data class OnNewLocation(val newLocation: Point?) : MapEvents()
}
