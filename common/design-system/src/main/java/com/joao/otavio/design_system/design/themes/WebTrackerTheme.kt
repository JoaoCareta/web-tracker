package com.joao.otavio.design_system.design.themes

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import com.joao.otavio.design_system.R
import androidx.compose.ui.text.font.FontFamily

interface WebTrackerTheme {
    val background: Color
    val lightBackground: Color

    val primary: Color
    val primaryLight: Color
    val primaryDark: Color
    val primaryText: Color
    val primaryIcon: Color

    val secondary: Color
    val secondaryDark: Color
    val secondaryText: Color
    val secondaryIcon: Color

    val third: Color
    val thirdDark: Color
    val thirdText: Color
    val thirdIcon: Color

    val outline: Color
    val outlineVariant: Color

    val secondaryButtonEnabled: Color

    val secondaryLineDivider: Color

    val neutralStatus: Color
    val lateStatus: Color
    val doneStatus: Color

    val error: Color

    object FontFamily {
        val Rubik = FontFamily(
            Font(R.font.rubik)
        )
    }

    companion object : WebTrackerTheme {
        var current: WebTrackerTheme = MainTheme()

        fun setTheme(theme: AppThemes) {
            when (theme) {
                AppThemes.LIGHT -> switchIf(current is DarkTheme) { MainTheme() }
                AppThemes.DARK -> switchIf(current is MainTheme) { DarkTheme() }
            }
        }

        private fun switchIf(predicate: Boolean, newTheme: () -> WebTrackerTheme) {
            if (predicate) current = newTheme()
        }

        override val background get() = current.background
        override val lightBackground get() = current.lightBackground

        override val primary get() = current.primary
        override val primaryLight get() = current.primaryLight
        override val primaryDark get() = current.primaryDark
        override val primaryText get() = current.primaryText
        override val primaryIcon get() = current.primaryIcon

        override val secondary get() = current.secondary
        override val secondaryDark get() = current.secondaryDark
        override val secondaryText get() = current.secondaryText
        override val secondaryIcon get() = current.secondaryIcon

        override val third get() = current.third
        override val thirdDark get() = current.thirdDark
        override val thirdText get() = current.thirdText
        override val thirdIcon get() = current.thirdIcon

        override val neutralStatus get() = current.neutralStatus
        override val lateStatus get() = current.lateStatus
        override val doneStatus get() = current.doneStatus

        override val outline get() = current.outline
        override val outlineVariant get() = current.outlineVariant

        override val secondaryButtonEnabled get() = current.secondaryButtonEnabled

        override val secondaryLineDivider get() = current.secondaryLineDivider
        override val error get() = current.error
    }
}
