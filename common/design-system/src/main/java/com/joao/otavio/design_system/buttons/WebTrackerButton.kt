package com.joao.otavio.design_system.buttons

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.joao.otavio.design_system.design.themes.DarkTheme
import com.joao.otavio.design_system.design.themes.MainTheme
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.dimensions.LocalDimensions
import com.joao.otavio.design_system.dimensions.LocalFontSize
import com.joao.otavio.utils.click.ClickUtils.doIfCanClick

@Composable
fun WebTrackerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    theme: WebTrackerTheme = WebTrackerTheme.current
) {
    val dimensionsValues = LocalDimensions.current
    val configuration = LocalConfiguration.current
    val fontSize = LocalFontSize.current
    val fontScale = configuration.fontScale
    val adjustedPadding = dimensionsValues.xSmall / fontScale.coerceAtLeast(1f)

    Button(
        onClick = {
            doIfCanClick {
                onClick.invoke()
            }
        },
        modifier = modifier
            .padding(dimensionsValues.medium)
            .fillMaxWidth()
            .height(dimensionsValues.xHuge),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = theme.secondary,
            contentColor = MainTheme().primary,
            disabledContainerColor = theme.secondaryButtonEnabled,
            disabledContentColor = theme.thirdText
        ),
        shape = RoundedCornerShape(dimensionsValues.xxxSmall),
        contentPadding = PaddingValues(horizontal = adjustedPadding),
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = fontSize.xSmall,
                fontFamily = WebTrackerTheme.FontFamily.Rubik,
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WebTrackerMainThemeButtonPreview() {
    WebTrackerButton(
        text = CLICK_ME,
        onClick = {},
    )
}

@Preview(showBackground = true)
@Composable
fun WebTrackerMainThemeButtonDisabledPreview() {
    WebTrackerButton(
        text = CLICK_ME,
        onClick = {},
        enabled = false,
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF121212
)
@Composable
fun WebTrackerDarkThemeButtonPreview() {
    WebTrackerButton(
        text = CLICK_ME,
        onClick = {},
        theme = DarkTheme()
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF121212
)
@Composable
fun WebTrackerDarkThemeButtonDisabledPreview() {
    WebTrackerButton(
        text = CLICK_ME,
        onClick = {},
        enabled = false,
        theme = DarkTheme()
    )
}

const val CLICK_ME = "Click me"
