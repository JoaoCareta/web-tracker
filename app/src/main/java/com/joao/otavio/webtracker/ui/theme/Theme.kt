package com.joao.otavio.webtracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.joao.otavio.design_system.design.themes.*
import com.joao.otavio.design_system.design.utils.ThemeUtils
import com.joao.otavio.design_system.dimensions.LocalFontSize


@Composable
fun WebTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    DisposableEffect(darkTheme) {
        ThemeUtils.setAppTheme(if (darkTheme) AppThemes.DARK.name else AppThemes.LIGHT.name)
        onDispose { }
    }

    val colorScheme = ColorScheme(
        primary = WebTrackerTheme.primary,
        onPrimary = WebTrackerTheme.primaryText,
        primaryContainer = WebTrackerTheme.primaryLight,
        onPrimaryContainer = WebTrackerTheme.primaryText,
        secondary = WebTrackerTheme.secondary,
        onSecondary = WebTrackerTheme.secondaryText,
        secondaryContainer = WebTrackerTheme.secondaryDark,
        onSecondaryContainer = WebTrackerTheme.secondaryText,
        tertiary = WebTrackerTheme.third,
        onTertiary = WebTrackerTheme.thirdText,
        tertiaryContainer = WebTrackerTheme.thirdDark,
        onTertiaryContainer = WebTrackerTheme.thirdText,
        error = WebTrackerTheme.error,
        onError = Color.White,
        errorContainer = WebTrackerTheme.error.copy(alpha = 0.8f),
        onErrorContainer = Color.White,
        background = WebTrackerTheme.background,
        onBackground = WebTrackerTheme.primaryText,
        surface = WebTrackerTheme.background,
        onSurface = WebTrackerTheme.primaryText,
        surfaceVariant = WebTrackerTheme.lightBackground,
        onSurfaceVariant = WebTrackerTheme.primaryText,
        outline = WebTrackerTheme.outline,
        outlineVariant = WebTrackerTheme.outlineVariant,
        scrim = Color.Black.copy(alpha = 0.3f),
        surfaceTint = WebTrackerTheme.primary,
        inversePrimary = WebTrackerTheme.primaryLight,
        inverseSurface = WebTrackerTheme.background,
        inverseOnSurface = WebTrackerTheme.primaryText,
    )

    val typography = Typography(
        bodyLarge = TextStyle(
            fontFamily = WebTrackerTheme.FontFamily.Rubik,
            fontWeight = FontWeight.Normal,
            fontSize = LocalFontSize.current.xSmall,
            lineHeight = LocalFontSize.current.medium,
            letterSpacing = LocalFontSize.current.xNano,
            color = WebTrackerTheme.primaryText
        )
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
