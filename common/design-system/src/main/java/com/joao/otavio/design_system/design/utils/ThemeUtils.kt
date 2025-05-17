package com.joao.otavio.design_system.design.utils
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.design.utils.ThemeUtils.AppThemes.LIGHT
import com.joao.otavio.design_system.design.utils.ThemeUtils.AppThemes.DARK

object ThemeUtils {
    enum class AppThemes {
        LIGHT, DARK
    }

    private var _currentSelectedTheme = LIGHT

    fun setAppTheme(themeName: String?) {
        val theme = getThemeTypeByName(themeName)
        _currentSelectedTheme = theme
        WebTrackerTheme.setTheme(theme.parse())
    }

    private fun getThemeTypeByName(themeName: String?): AppThemes {
        return when (themeName) {
            LIGHT.name -> {
                LIGHT
            }
            DARK.name -> {
                DARK
            }
            else -> {
                LIGHT
            }
        }
    }

    private fun AppThemes.parse() = when(this) {
        LIGHT -> com.joao.otavio.design_system.design.themes.AppThemes.LIGHT
        DARK -> com.joao.otavio.design_system.design.themes.AppThemes.DARK
    }
}
