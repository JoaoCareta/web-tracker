package com.joao.otavio.design_system.dimensions

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Margins(
    val nano: Dp = 1.dp,
    val micro: Dp = 2.dp,
    val tiny: Dp = 3.dp,
    val mini: Dp = 4.dp,
    val xxxSmall: Dp = 8.dp,
    val xxSmall: Dp = 12.dp,
    val xSmall: Dp = 16.dp,
    val small: Dp = 20.dp,
    val medium: Dp = 24.dp,
    val large: Dp = 28.dp,
    val xLarge: Dp = 32.dp,
    val xxLarge: Dp = 36.dp,
    val xxxLarge: Dp = 40.dp,
    val huge: Dp = 44.dp,
    val xHuge: Dp = 48.dp,
    val xxHuge: Dp = 52.dp,
    val xxxHuge: Dp = 56.dp,
    val giant: Dp = 60.dp,
    val xGiant: Dp = 64.dp,
    val xxGiant: Dp = 68.dp,
    val xxxGiant: Dp = 72.dp,
    val mega: Dp = 76.dp,
    val xMega: Dp = 80.dp,
    val xxMega: Dp = 84.dp,
    val xxxMega: Dp = 88.dp,
    val ultra: Dp = 92.dp,
    val xUltra: Dp = 96.dp,
    val xxUltra: Dp = 100.dp
)

val LocalMargins = compositionLocalOf { Margins() }
