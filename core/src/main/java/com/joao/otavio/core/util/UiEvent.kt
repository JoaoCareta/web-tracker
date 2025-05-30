package com.joao.otavio.core.util

sealed class UiEvent {
    data class Navigate(
        val route: String,
        val popUpToRoute: String? = null,
        val inclusive: Boolean = false
    ): UiEvent()

    object NavigateUp: UiEvent()
}
