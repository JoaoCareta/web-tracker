package com.joao.otavio.design_system.divider

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joao.otavio.design_system.design.themes.DarkTheme
import com.joao.otavio.design_system.design.themes.MainTheme
import com.joao.otavio.design_system.design.themes.WebTrackerTheme

@Composable
fun Divider(modifier: Modifier = Modifier, theme: WebTrackerTheme = WebTrackerTheme) =
    HorizontalDivider(
        color = theme.secondaryLineDivider,
        modifier = modifier
    )

@Preview(name = "Main Theme")
@Composable
private fun DividerPreview() = Divider(theme = MainTheme())

@Preview(name = "Main Theme")
@Composable
private fun DividerDarkPreview() = Divider(theme = DarkTheme())
