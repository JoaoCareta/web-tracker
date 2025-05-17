package com.joao.otavio.design_system.dimensions

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

data class FontSize(
    val xNano: TextUnit = 0.5.sp,
    val nano: TextUnit = 1.sp,
    val micro: TextUnit = 2.sp,
    val tiny: TextUnit = 3.sp,
    val mini: TextUnit = 4.sp,
    val xxxSmall: TextUnit = 8.sp,
    val xxSmall: TextUnit = 12.sp,
    val xSmall: TextUnit = 16.sp,
    val small: TextUnit = 20.sp,
    val medium: TextUnit = 24.sp,
    val large: TextUnit = 28.sp,
    val xLarge: TextUnit = 32.sp,
    val xxLarge: TextUnit = 36.sp,
    val xxxLarge: TextUnit = 40.sp,
    val huge: TextUnit = 44.sp,
    val xHuge: TextUnit = 48.sp,
    val xxHuge: TextUnit = 52.sp,
    val xxxHuge: TextUnit = 56.sp,
    val giant: TextUnit = 60.sp,
    val xGiant: TextUnit = 64.sp,
    val xxGiant: TextUnit = 68.sp,
    val xxxGiant: TextUnit = 72.sp,
    val mega: TextUnit = 76.sp,
    val xMega: TextUnit = 80.sp,
    val xxMega: TextUnit = 84.sp,
    val xxxMega: TextUnit = 88.sp,
    val ultra: TextUnit = 92.sp,
    val xUltra: TextUnit = 96.sp,
    val xxUltra: TextUnit = 100.sp
)

val LocalFontSize = compositionLocalOf { FontSize() }
