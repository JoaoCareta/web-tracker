package com.joao.otavio.design_system.headers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.dimensions.LocalDimensions
import com.joao.otavio.design_system.dimensions.LocalPaddings

@Composable
fun HeaderCard(
    headerText: String,
    onInitialTextClick: () -> Unit = {},
    webTrackerTheme: WebTrackerTheme = WebTrackerTheme.current
) {
    val paddings = LocalPaddings.current
    val dimensions = LocalDimensions.current
    Box(
        modifier = Modifier
            .offset(y = -paddings.xSmall)
            .padding(
                start = paddings.xLarge,
                end = paddings.xLarge,
            )
            .fillMaxWidth()
            .height(dimensions.giant)
            .border(
                border = BorderStroke(dimensions.xNano, color = webTrackerTheme.primaryDark),
                shape = RoundedCornerShape(dimensions.xxxSmall),
            )
            .shadow(
                elevation = dimensions.xxSmall,
                shape = RoundedCornerShape(dimensions.xxxSmall),
                spotColor = webTrackerTheme.background
            )
            .clip(RoundedCornerShape(dimensions.xxxSmall))
            .background(webTrackerTheme.lightBackground),
    ) {
        InitialsWithText(
            text = headerText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = paddings.xSmall)
                .clickable { onInitialTextClick.invoke() },
            theme = webTrackerTheme
        )
    }
}

@Composable
fun InitialsWithText(
    text: String,
    modifier: Modifier = Modifier,
    theme: WebTrackerTheme = WebTrackerTheme,
    backgroundColor: Color = theme.primaryLight,
    textColor: Color = theme.primaryText,
    initialsColor: Color = theme.primaryIcon
) {
    val paddings = LocalPaddings.current
    val dimensions = LocalDimensions.current
    Row(
        modifier = modifier.padding(
            top = paddings.xxSmall,
            start = paddings.xSmall,
            bottom = paddings.mini,
            end = paddings.xSmall
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .size(dimensions.xxLarge)
                .clip(CircleShape)
                .border(
                    border = BorderStroke(dimensions.xNano, color = theme.secondaryDark),
                    shape = CircleShape,
                )
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text.split(" ")
                    .take(2)
                    .mapNotNull { it.firstOrNull()?.uppercase() }
                    .joinToString(""),
                style = MaterialTheme.typography.titleMedium,
                color = initialsColor
            )
        }

        Spacer(modifier = Modifier.width(dimensions.xxSmall))

        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )
    }
}
