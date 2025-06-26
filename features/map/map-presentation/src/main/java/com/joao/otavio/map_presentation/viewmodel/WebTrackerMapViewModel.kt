package com.joao.otavio.map_presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.joao.otavio.map_presentation.events.MapEvents
import com.joao.otavio.map_presentation.state.WebTrackerMapState
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WebTrackerMapViewModel @Inject constructor() : ViewModel() {
    val webTrackerMapState: WebTrackerMapState = WebTrackerMapState()

    fun onUiEvents(mapEvents: MapEvents) {
        when(mapEvents) {
            is MapEvents.OnMapZoomChanged -> handleOnMapZoomChanged(mapEvents.newZoomValue)
            is MapEvents.OnNewLocation -> handleOnNewLocation(mapEvents.newLocation)
        }
    }

    private fun handleOnMapZoomChanged(newMapZoomValue: Double?) {
        newMapZoomValue?.let { newZoomValue ->
            webTrackerMapState.mapZoom.update { newZoomValue }
        }
    }

    private fun handleOnNewLocation(newLocation: Point?) {
        webTrackerMapState.lastKnowLocation.update { newLocation }
    }
}
