package com.joao.otavio.design_system.design.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.joao.otavio.design_system.design.utils.ThemeUtils
import com.joao.otavio.design_system.dimensions.LocalAlpha
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

    val alpha = LocalAlpha.current

    val colorScheme = ColorScheme(
        // Primary colors
        primary = WebTrackerTheme.primary,
        onPrimary = WebTrackerTheme.primaryText,
        primaryContainer = WebTrackerTheme.primaryLight,
        onPrimaryContainer = WebTrackerTheme.primaryText,
        inversePrimary = WebTrackerTheme.primaryLight,

        // Secondary colors
        secondary = WebTrackerTheme.secondary,
        onSecondary = WebTrackerTheme.secondaryText,
        secondaryContainer = WebTrackerTheme.secondaryDark,
        onSecondaryContainer = WebTrackerTheme.secondaryText,

        // Tertiary colors
        tertiary = WebTrackerTheme.third,
        onTertiary = WebTrackerTheme.thirdText,
        tertiaryContainer = WebTrackerTheme.thirdDark,
        onTertiaryContainer = WebTrackerTheme.thirdText,

        // Background and surface colors
        background = WebTrackerTheme.background,
        onBackground = WebTrackerTheme.primaryText,
        surface = WebTrackerTheme.background,
        onSurface = WebTrackerTheme.primaryText,
        surfaceVariant = WebTrackerTheme.lightBackground,
        onSurfaceVariant = WebTrackerTheme.primaryText,
        surfaceTint = WebTrackerTheme.primary,
        inverseSurface = WebTrackerTheme.background,
        inverseOnSurface = WebTrackerTheme.primaryText,

        // Surface container colors
        surfaceBright = WebTrackerTheme.background,
        surfaceDim = WebTrackerTheme.background.copy(alpha = alpha.emphasizedHigh),
        surfaceContainer = WebTrackerTheme.background,
        surfaceContainerHigh = WebTrackerTheme.background.copy(alpha = alpha.nearOpaque),
        surfaceContainerHighest = WebTrackerTheme.background,
        surfaceContainerLow = WebTrackerTheme.background.copy(alpha = alpha.almostOpaque),
        surfaceContainerLowest = WebTrackerTheme.background.copy(alpha = alpha.barelyTransparent),

        // Error colors
        error = WebTrackerTheme.error,
        onError = Color.White,
        errorContainer = WebTrackerTheme.error.copy(alpha = 0.8f),
        onErrorContainer = Color.White,

        // Other colors
        outline = WebTrackerTheme.outline,
        outlineVariant = WebTrackerTheme.outlineVariant,
        scrim = Color.Black.copy(alpha = 0.3f)
    )

    val typography = Typography(
        headlineLarge = TextStyle(
            fontFamily = WebTrackerTheme.FontFamily.Rubik,
            fontWeight = FontWeight.Bold,
            fontSize = LocalFontSize.current.xxxLarge,
            lineHeight = LocalFontSize.current.huge,
            letterSpacing = LocalFontSize.current.nano,
            color = WebTrackerTheme.primaryText
        ),
        titleLarge = TextStyle(
            fontFamily = WebTrackerTheme.FontFamily.Rubik,
            fontWeight = FontWeight.Medium,
            fontSize = LocalFontSize.current.large,
            lineHeight = LocalFontSize.current.xLarge,
            letterSpacing = LocalFontSize.current.nano,
            color = WebTrackerTheme.primaryText
        ),
        bodySmall = TextStyle(
            fontFamily = WebTrackerTheme.FontFamily.Rubik,
            fontWeight = FontWeight.Normal,
            fontSize = LocalFontSize.current.small,
            lineHeight = LocalFontSize.current.medium,
            letterSpacing = LocalFontSize.current.xNano,
            color = WebTrackerTheme.primaryText
        ),
        bodyLarge = TextStyle(
            fontFamily = WebTrackerTheme.FontFamily.Rubik,
            fontWeight = FontWeight.Normal,
            fontSize = LocalFontSize.current.xSmall,
            lineHeight = LocalFontSize.current.medium,
            letterSpacing = LocalFontSize.current.xNano,
            color = WebTrackerTheme.primaryText
        ),
        displaySmall = TextStyle(
            fontFamily = WebTrackerTheme.FontFamily.Rubik,
            fontWeight = FontWeight.Thin,
            fontSize = LocalFontSize.current.xxSmall,
            lineHeight = LocalFontSize.current.medium,
            letterSpacing = LocalFontSize.current.xNano,
            color = WebTrackerTheme.thirdText
        )
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
