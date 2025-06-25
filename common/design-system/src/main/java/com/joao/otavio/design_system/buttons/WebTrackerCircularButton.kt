package com.joao.otavio.design_system.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.dimensions.LocalDimensions
import com.joao.otavio.design_system.dimensions.LocalPaddings
import com.joao.otavio.utils.click.ClickUtils.doIfCanClick
import com.joao.otavio.webtracker.common.desygn.system.R

@Composable
fun WebTrackerCircularButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    theme: WebTrackerTheme = WebTrackerTheme,
    isActive: Boolean = true,
    clickInterval: Long = 0L
) {
    val dimensionsValues = LocalDimensions.current
    val paddingValues = LocalPaddings.current

    Button(
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = dimensionsValues.none,
            pressedElevation = dimensionsValues.none
        ),
        onClick = {
            doIfCanClick(interval = clickInterval) {
                onClick.invoke()
            }
        },
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) theme.secondaryDark else theme.secondaryButtonEnabled,
            contentColor = if (isActive) theme.primary else theme.thirdText
        ),
        contentPadding = PaddingValues(paddingValues.none),
        modifier = modifier
            .width(dimensionsValues.xHuge)
            .height(dimensionsValues.xHuge)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null
        )
    }
}

@Composable
fun WebTrackerCircularButton(
    icon: ImageVector,
    onClick: () -> Unit,
    theme: WebTrackerTheme,
    modifier: Modifier = Modifier,
    isActive: Boolean = true
) {
    val dimensionsValues = LocalDimensions.current
    val paddingValues = LocalPaddings.current

    Button(
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = dimensionsValues.none,
            pressedElevation = dimensionsValues.none
        ),
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) theme.secondaryDark else theme.secondaryButtonEnabled,
            contentColor = if (isActive) theme.primary else theme.thirdText
        ),
        contentPadding = PaddingValues(paddingValues.none),
        modifier = modifier
            .width(dimensionsValues.xHuge)
            .height(dimensionsValues.xHuge)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun WebTrackerCircularButtonPreview() {
    WebTrackerCircularButton(
        icon = R.drawable.ic_location,
        onClick = {

        },
    )
}
