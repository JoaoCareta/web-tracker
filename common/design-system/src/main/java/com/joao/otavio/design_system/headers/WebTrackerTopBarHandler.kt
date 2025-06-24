package com.joao.otavio.design_system.headers

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.joao.otavio.core.util.isEmptyOrBlank
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.webtracker.common.desygn.system.R

@Composable
fun WebTrackerTopBarHandler(
    title: String,
    onClickLeftIcon: Painter = painterResource(R.drawable.ic_arrow_left),
    onClickLeft: (() -> Unit)? = null,
    onClickRightIcon: Painter = painterResource(R.drawable.ic_search),
    onClickRight: (() -> Unit)? = null,
    theme: WebTrackerTheme = WebTrackerTheme,
    headerText: String = "",
    onInitialTextClick: () -> Unit = {},
    backgroundColor: Color = theme.secondary,
    iconsColor: Color = theme.secondaryDark
) {
    if (headerText.isEmptyOrBlank()) {
        LightHeader(
            title = title,
            onClickLeftIcon = onClickLeftIcon,
            onClickLeft = onClickLeft,
            onClickRightIcon = onClickRightIcon,
            onClickRight = onClickRight,
            theme = theme,
            backgroundColor = backgroundColor,
            iconsColor = iconsColor
        )
    } else {
        HeavyHeader(
            title = title,
            onClickLeftIcon = onClickLeftIcon,
            onClickLeft = onClickLeft,
            onClickRightIcon = onClickRightIcon,
            onClickRight = onClickRight,
            theme = theme,
            backgroundColor = backgroundColor,
            iconsColor = iconsColor
        )

        HeaderCard(
            headerText = headerText,
            webTrackerTheme = theme,
            onInitialTextClick = onInitialTextClick
        )
    }
}
