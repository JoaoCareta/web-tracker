package com.joao.otavio.design_system.dimensions

import androidx.compose.runtime.compositionLocalOf

data class Alpha(
    val transparent: Float = 0f,
    val ultraLight: Float = 0.1f,
    val extraLight: Float = 0.2f,
    val light: Float = 0.3f,
    val mediumLight: Float = 0.4f,
    val medium: Float = 0.5f,
    val mediumHigh: Float = 0.6f,
    val high: Float = 0.7f,
    val extraHigh: Float = 0.8f,
    val emphasizedHigh: Float = 0.87f,
    val ultraHigh: Float = 0.9f,
    val nearOpaque: Float = 0.92f,
    val almostOpaque: Float = 0.96f,
    val barelyTransparent: Float = 0.98f,
    val opaque: Float = 1f,


    val disabled: Float = 0.38f,
    val hoverOverlay: Float = 0.08f,
    val pressedOverlay: Float = 0.12f,
    val scrim: Float = 0.32f,
    val divider: Float = 0.12f
)

val LocalAlpha = compositionLocalOf { Alpha() }
