package com.joao.otavio.core.util

sealed class NavigationEvent {
    data class Navigate(
        val route: NavigationCommand,
        val navigationType: NavigationType,
        val popUpToRoute: NavigationCommand? = null,
        val inclusive: Boolean = false,
        val restoreState: Boolean = false
    ): NavigationEvent()

    object NavigateUp: NavigationEvent()

    object PopBackStack: NavigationEvent()
}

interface NavigationCommand {
    val route: String
    fun createRoute(vararg args: String): String = route
}

interface NavigationArgs {
    val key: String
    val defaultValue: Any?
}

enum class NavigationType {
    ADD_TO_STACK,
    REPLACE_STACK,
    SINGLE_TOP,
    CLEAR_UNTIL_ROUTE
}
