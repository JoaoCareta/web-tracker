package com.joao.otavio.core.navigation

sealed class WebTrackerScreens(
    val route: String,
) {
    data object Authentication : WebTrackerScreens(
        route = "authentication?version={version}",
    ) {
        const val VERSION_KEY = "version"
        fun createRoute(version: String) = "authentication?version=$version"
    }

    data object EmployeeIdentification : WebTrackerScreens(
        route = "employeeIdentification",
    )
}
