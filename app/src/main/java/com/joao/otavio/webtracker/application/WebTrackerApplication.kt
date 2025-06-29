package com.joao.otavio.webtracker.application

import android.app.Application
import android.content.res.Configuration
import com.joao.otavio.design_system.design.themes.AppThemes
import com.joao.otavio.design_system.design.utils.ThemeUtils
import com.joao.otavio.webtracker.BuildConfig
import com.mapbox.common.MapboxOptions
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WebTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MapboxOptions.accessToken = BuildConfig.MAPBOX_ACCESS_TOKEN
        setInitialTheme()
    }

    private fun setInitialTheme() {
        val isDarkTheme = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
        ThemeUtils.setAppTheme(if (isDarkTheme) AppThemes.DARK.name else AppThemes.LIGHT.name)
    }
}
