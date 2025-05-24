package com.joao.otavio.webtracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.joao.otavio.authentication_presentation.ui.screens.DummyScreen
import com.joao.otavio.core.util.UiEvent
import com.joao.otavio.authentication_presentation.ui.screens.login.LoginScreen
import com.joao.otavio.core.navigation.WebTrackerScreens

@Composable
fun WebTrackerNavigation(appVersion: String) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = WebTrackerScreens.Login.createRoute(appVersion)
    ) {
        composable(
            route = WebTrackerScreens.Login.route,
            arguments = listOf(
                navArgument(WebTrackerScreens.Login.VERSION_KEY) {
                    type = NavType.StringType
                    defaultValue = "1.0.0"
                }
            )
        ) { backStackEntry ->
            val version = backStackEntry.arguments?.getString(WebTrackerScreens.Login.VERSION_KEY) ?: "1.0.0"
            LoginScreen(
                version = version,
                onEnterClick = navController::navigateTo
            )
        }

        composable(
            route = WebTrackerScreens.Dummy.route
        ) {
            DummyScreen()
        }
    }
}

fun NavController.navigateTo(event: UiEvent.Navigate) {
    this.navigate(event.route)
}
