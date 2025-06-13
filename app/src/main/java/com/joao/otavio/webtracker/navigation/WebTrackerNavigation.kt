package com.joao.otavio.webtracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.joao.otavio.identification_presentation.ui.screens.identification.EmployeeIdentificationScreen
import com.joao.otavio.core.util.UiEvent
import com.joao.otavio.authentication_presentation.ui.screens.authentication.AuthenticationScreen
import com.joao.otavio.core.navigation.WebTrackerScreens

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
                navArgument(WebTrackerScreens.Authentication.VERSION_KEY) {
                    type = NavType.StringType
                    defaultValue = "1.0.0"
                }
            )
        ) { backStackEntry ->
            val version = backStackEntry.arguments?.getString(WebTrackerScreens.Authentication.VERSION_KEY) ?: "1.0.0"
            AuthenticationScreen(
                version = version,
                onEnterClick = navController::navigateTo
            )
        }

        composable(
            route = WebTrackerScreens.EmployeeIdentification.route
        ) {
            com.joao.otavio.identification_presentation.ui.screens.identification.EmployeeIdentificationScreen()
        }
    }
}

fun NavController.navigateTo(event: UiEvent.Navigate) {
    this.navigate(event.route) {
        event.popUpToRoute?.let { route ->
            popUpTo(route) {
                inclusive = event.inclusive
            }
        }
    }
}
