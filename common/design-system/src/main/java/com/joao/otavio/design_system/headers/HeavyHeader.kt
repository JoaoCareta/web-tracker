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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.joao.otavio.design_system.design.themes.DarkTheme
import com.joao.otavio.design_system.design.themes.MainTheme
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.dimensions.LocalAlpha
import com.joao.otavio.design_system.dimensions.LocalFontSize
import com.joao.otavio.design_system.dimensions.LocalPaddings
import com.joao.otavio.design_system.dimensions.LocalDimensions
import com.joao.otavio.webtracker.common.desygn.system.R

@Composable
fun HeavyHeader(
    title: String,
    onClickLeftIcon: Painter = painterResource(R.drawable.ic_arrow_left),
    onClickLeft: (() -> Unit)? = null,
    onClickRightIcon: Painter = painterResource(R.drawable.ic_search),
    onClickRight: (() -> Unit)? = null,
    theme: WebTrackerTheme = WebTrackerTheme,
    backgroundColor: Color = theme.secondary,
    iconsColor: Color = theme.secondaryDark
) {
    val paddingValues = LocalPaddings.current
    val dimensionsValues = LocalDimensions.current
    val fontValues = LocalFontSize.current
    val alpha = LocalAlpha.current

    Box {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(backgroundColor)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(
                    start = paddingValues.xSmall,
                    end = paddingValues.xSmall,
                    bottom = paddingValues.large,
                    top = paddingValues.xSmall
                )
        ) {
            Button(
                elevation = ButtonDefaults.buttonElevation(dimensionsValues.none, dimensionsValues.none),
                onClick = { onClickLeft?.invoke() },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = iconsColor
                ),
                contentPadding = PaddingValues(paddingValues.none),
                modifier = Modifier
                    .width(dimensionsValues.xHuge)
                    .height(dimensionsValues.xHuge)
                    .alpha(onClickLeft?.let { alpha.opaque } ?: alpha.transparent)
            ) {
                Icon(
                    painter = onClickLeftIcon,
                    tint = MainTheme().primary,
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
                color = MainTheme().primary,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
            )

            Button(
                elevation = ButtonDefaults.buttonElevation(dimensionsValues.none, dimensionsValues.none),
                onClick = { onClickRight?.invoke() },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = iconsColor
                ),
                contentPadding = PaddingValues(paddingValues.none),
                modifier = Modifier
                    .width(dimensionsValues.xHuge)
                    .height(dimensionsValues.xHuge)
                    .alpha(onClickRight?.let { alpha.opaque } ?: alpha.transparent)
            ) {
                Icon(
                    painter = onClickRightIcon,
                    tint = MainTheme().primary,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun LightHeaderPreview() {
    HeavyHeader(
        "Web Tracker Organization",
        onClickLeft = {},
        onClickRight = {},
        theme = MainTheme()
    )
}

@Preview
@Composable
private fun LightHeaderDarkPreview() {
    HeavyHeader(
        "Web Tracker - Preview with two lines",
        onClickLeft = {},
        onClickRight = {},
        theme = DarkTheme()
    )
}

@Preview
@Composable
private fun LightHeaderTextOverLimitPreview() {
    HeavyHeader(
        "Web Tracker - Preview with more than two lines to preview",
        onClickLeft = {},
        onClickRight = {},
        theme = MainTheme()
    )
}

@Preview
@Composable
private fun LightHeaderTextOverLimitDarkPreview() {
    HeavyHeader(
        "Web Tracker - Preview with more than two lines to preview",
        onClickLeft = {},
        onClickRight = {},
        theme = DarkTheme()
    )
}
