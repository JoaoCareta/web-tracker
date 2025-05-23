package com.joao.otavio.webtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.joao.otavio.design_system.design.themes.*
import com.joao.otavio.webtracker.navigation.WebTrackerNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WebTrackerTheme {
                WebTrackerApplication()
            }
        }
    }
}

@Composable
fun WebTrackerApplication() {
    val appVersion = BuildConfig.VERSION_NAME
    WebTrackerNavigation(appVersion)
}
