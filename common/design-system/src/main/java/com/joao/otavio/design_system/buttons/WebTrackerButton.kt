package com.joao.otavio.design_system.buttons

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joao.otavio.design_system.design.themes.DarkTheme
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.dimensions.LocalDimensions

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
    val fontScale = configuration.fontScale
    val adjustedPadding = dimensionsValues.xSmall / fontScale.coerceAtLeast(1f)

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionsValues.xHuge),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = theme.secondary,
            contentColor = theme.primary,
            disabledContainerColor = theme.secondaryButtonEnabled,
            disabledContentColor = theme.thirdText
        ),
        shape = RoundedCornerShape(dimensionsValues.xxxSmall),
        contentPadding = PaddingValues(horizontal = adjustedPadding),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = MaterialTheme.typography.labelLarge.fontSize / fontScale
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
        modifier = Modifier.padding(24.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun WebTrackerMainThemeButtonDisabledPreview() {
    WebTrackerButton(
        text = CLICK_ME,
        onClick = {},
        enabled = false,
        modifier = Modifier.padding(24.dp)
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
        modifier = Modifier.padding(24.dp),
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
        modifier = Modifier.padding(24.dp),
        theme = DarkTheme()
    )
}

const val CLICK_ME = "Click me"
