package com.joao.otavio.core.navigation

sealed class WebTrackerScreens(
    val route: String,
    val title: String,
    val baseRoute: String = route,
    val hasArguments: Boolean = false
) {
    data object Login : WebTrackerScreens(
        route = "login?version={version}",
        title = "Login",
        baseRoute = "login",
        hasArguments = true
    ) {
        const val VERSION_KEY = "version"

        fun createRoute(version: String) = "login?version=$version"
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
                Login.baseRoute -> Login
                null -> Login
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
        }
    }
}
