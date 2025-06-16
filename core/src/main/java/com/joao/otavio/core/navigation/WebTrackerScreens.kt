package com.joao.otavio.core.navigation

import com.joao.otavio.core.util.NavigationArgs
import com.joao.otavio.core.util.NavigationCommand
import com.joao.otavio.core.util.NavigationEvent
import com.joao.otavio.core.util.NavigationType

sealed class WebTrackerScreens : NavigationCommand {
    data object Authentication : WebTrackerScreens() {
        override val route = "authentication?version={version}"

        object Args {
            object Version : NavigationArgs {
                override val key = "version"
                override val defaultValue = "1.0.0"
            }
        }

        override fun createRoute(vararg args: String): String {
            require(args.size == 1) { "Authentication requires version parameter" }
            return "authentication?version=${args[0]}"
        }
    }

    data object EmployeeIdentification : WebTrackerScreens() {
        override val route = "employeeIdentification"
    }

    data object MissingPermissions : WebTrackerScreens() {
        override val route = "missingPermissions"
    }

    fun navigateReplacingStack(): NavigationEvent.Navigate = NavigationEvent.Navigate(
        route = this,
        navigationType = NavigationType.REPLACE_STACK
    )

    fun navigateKeepingInStack(): NavigationEvent.Navigate = NavigationEvent.Navigate(
        route = this,
        navigationType = NavigationType.ADD_TO_STACK
    )

    fun navigateSingleTop(
        restoreState: Boolean = true
    ): NavigationEvent.Navigate = NavigationEvent.Navigate(
        route = this,
        navigationType = NavigationType.SINGLE_TOP,
        restoreState = restoreState
    )

    fun navigateClearingUntil(untilRoute: NavigationCommand): NavigationEvent.Navigate = NavigationEvent.Navigate(
        route = this,
        navigationType = NavigationType.CLEAR_UNTIL_ROUTE,
        popUpToRoute = untilRoute
    )
}
