package com.joao.otavio.webtracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.joao.otavio.authentication_presentation.ui.screens.authentication.AuthenticationScreen
import com.joao.otavio.core.navigation.WebTrackerScreens
import com.joao.otavio.core.util.NavigationEvent
import com.joao.otavio.core.util.NavigationType
import com.joao.otavio.design_system.missingPermissions.WebTrackerMissingPermissionsScreen
import com.joao.otavio.identification_presentation.ui.screens.identification.EmployeeIdentificationScreen

@Composable
fun WebTrackerNavigation(appVersion: String) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = WebTrackerScreens.Authentication.createRoute(appVersion)
    ) {
        composable(
            route = WebTrackerScreens.Authentication.route,
            arguments = listOf(
                navArgument(WebTrackerScreens.Authentication.Args.Version.key) {
                    type = NavType.StringType
                    defaultValue = WebTrackerScreens.Authentication.Args.Version.defaultValue
                }
            )
        ) { backStackEntry ->
            val version = backStackEntry.arguments?.getString(WebTrackerScreens.Authentication.Args.Version.key)
                ?: WebTrackerScreens.Authentication.Args.Version.defaultValue
            AuthenticationScreen(
                version = version,
                onEnterClick = navController::navigateTo
            )
        }

        composable(
            route = WebTrackerScreens.EmployeeIdentification.route
        ) {
            EmployeeIdentificationScreen(
                navigation = navController::navigateTo
            )
        }

        composable(
            route = WebTrackerScreens.MissingPermissions.route
        ) {
            WebTrackerMissingPermissionsScreen(
                navigation = navController::navigateTo
            )
        }
    }
}

fun NavController.navigateTo(event: NavigationEvent) {
    when (event) {
        is NavigationEvent.Navigate -> {
            navigate(event.route.route) {
                when (event.navigationType) {
                    NavigationType.REPLACE_STACK -> {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                    NavigationType.SINGLE_TOP -> {
                        launchSingleTop = true
                        restoreState = event.restoreState
                    }
                    NavigationType.CLEAR_UNTIL_ROUTE -> {
                        event.popUpToRoute?.let { route ->
                            popUpTo(route.route) { inclusive = event.inclusive }
                        }
                    }
                    NavigationType.ADD_TO_STACK -> {
                        Unit
                    }
                }
            }
        }
        NavigationEvent.NavigateUp -> navigateUp()
        NavigationEvent.PopBackStack -> popBackStack()
    }
}
