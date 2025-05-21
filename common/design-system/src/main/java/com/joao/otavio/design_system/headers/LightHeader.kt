package com.joao.otavio.design_system.headers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.joao.otavio.design_system.design.themes.DarkTheme
import com.joao.otavio.design_system.design.themes.MainTheme
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.dimensions.LocalFontSize
import com.joao.otavio.design_system.dimensions.LocalPaddings
import com.joao.otavio.design_system.dimensions.LocalSpacing
import com.joao.otavio.webtracker.R

@Composable
fun LightHeader(
    title: String,
    onClickLeft: () -> Unit,
    onClickRight: (() -> Unit)? = null,
    theme: WebTrackerTheme = WebTrackerTheme,
) {
    val paddingValues = LocalPaddings.current
    val spacingValues = LocalSpacing.current
    val fontValues = LocalFontSize.current

    Box(Modifier.background(theme.primary)) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(theme.primary)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(
                    start = paddingValues.xSmall,
                    end = paddingValues.xSmall,
                    bottom = paddingValues.xxSmall,
                    top = paddingValues.xSmall
                )
        ) {
            Button(
                elevation = ButtonDefaults.buttonElevation(spacingValues.none, spacingValues.none),
                onClick = onClickLeft,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = theme.primaryDark
                ),
                contentPadding = PaddingValues(paddingValues.none),
                modifier = Modifier
                    .width(spacingValues.xHuge)
                    .height(spacingValues.xHuge)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    tint = theme.primaryIcon,
                    contentDescription = null
                )
            }

            Text(
                text = title,
                modifier = Modifier
                    .padding(horizontal = paddingValues.xSmall)
                    .weight(1f)
                    .fillMaxWidth(),
                fontSize = fontValues.small,
                fontWeight = FontWeight.Bold,
                color = theme.primaryText,
                maxLines = 2,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
            )

            Button(
                elevation = ButtonDefaults.buttonElevation(spacingValues.none, spacingValues.none),
                onClick = { onClickRight?.invoke() },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = theme.primaryDark
                ),
                contentPadding = PaddingValues(paddingValues.none),
                modifier = Modifier
                    .width(spacingValues.xHuge)
                    .height(spacingValues.xHuge)
                    .alpha(onClickRight?.let { 1f } ?: 0f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    tint = theme.primaryIcon,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun LightHeaderPreview() {
    LightHeader(
        "Web Tracker",
        onClickLeft = {},
        onClickRight = {},
        theme = MainTheme()
    )
}

@Preview
@Composable
private fun LightHeaderDarkPreview() {
    LightHeader(
        "Web Tracker - Preview with two lines",
        onClickLeft = {},
        onClickRight = {},
        theme = DarkTheme()
    )
}

@Preview
@Composable
private fun LightHeaderTextOverLimitPreview() {
    LightHeader(
        "Web Tracker - Preview with more than two lines to preview",
        onClickLeft = {},
        onClickRight = {},
        theme = MainTheme()
    )
}

@Preview
@Composable
private fun LightHeaderTextOverLimitDarkPreview() {
    LightHeader(
        "Web Tracker - Preview with more than two lines to preview",
        onClickLeft = {},
        onClickRight = {},
        theme = DarkTheme()
    )
}
