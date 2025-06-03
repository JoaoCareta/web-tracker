package com.joao.otavio.core.navigation

sealed class WebTrackerScreens(
    val route: String,
    val title: String,
    val baseRoute: String = route,
    val hasArguments: Boolean = false
) {
    data object Authentication : WebTrackerScreens(
        route = "authentication?version={version}",
        title = "Authentication",
        baseRoute = "authentication",
        hasArguments = true
    ) {
        const val VERSION_KEY = "version"

        fun createRoute(version: String) = "authentication?version=$version"
    }

    data object Dummy : WebTrackerScreens(
        route = "dummy",
        title = "Dummy",
        baseRoute = "dummy",
        hasArguments = false
    )

    companion object {
        fun fromRoute(route: String?): WebTrackerScreens {
            return when (route?.substringBefore("?")) {
                Authentication.baseRoute -> Authentication
                null -> Authentication
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
        }
    }
}
