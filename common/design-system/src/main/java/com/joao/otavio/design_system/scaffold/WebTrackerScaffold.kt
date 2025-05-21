package com.joao.otavio.design_system.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.joao.otavio.design_system.design.themes.DarkTheme
import com.joao.otavio.design_system.design.themes.MainTheme
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.headers.LightHeader

@Composable
fun WebTrackerScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackBarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = WebTrackerTheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackBarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        content = content,
    )
}

@Preview(showBackground = true)
@Composable
fun WebTrackerScaffoldPreviewMainTheme() {
    WebTrackerScaffold(
        topBar = {
            LightHeader(
                title = WEB_TRACKER,
                onClickLeft = {},
                onClickRight = {},
                theme = MainTheme()
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MainTheme().background)
        ) {
            Text(
                text = MOCKK_TEXT,
                modifier = Modifier.align(Alignment.Center),
                color = MainTheme().primaryText
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WebTrackerScaffoldSecondPreviewMainTheme() {
    WebTrackerScaffold(
        topBar = {
            LightHeader(
                title = WEB_TRACKER,
                onClickLeft = {},
                theme = MainTheme()
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MainTheme().background)
        ) {
            Text(
                text = MOCKK_TEXT,
                modifier = Modifier.align(Alignment.Center),
                color = MainTheme().primaryText
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WebTrackerScaffoldPreviewDarkTheme() {
    WebTrackerScaffold(
        topBar = {
            LightHeader(
                title = WEB_TRACKER,
                onClickLeft = {},
                onClickRight = {},
                theme = DarkTheme()
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(DarkTheme().background)
        ) {
            Text(
                text = MOCKK_TEXT,
                modifier = Modifier.align(Alignment.Center),
                color = DarkTheme().primaryText
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WebTrackerScaffoldSecondPreviewDarkTheme() {
    WebTrackerScaffold(
        topBar = {
            LightHeader(
                title = WEB_TRACKER,
                onClickLeft = {},
                theme = DarkTheme()
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(DarkTheme().background)
        ) {
            Text(
                text = MOCKK_TEXT,
                modifier = Modifier.align(Alignment.Center),
                color = DarkTheme().primaryText
            )
        }
    }
}

const val WEB_TRACKER = "Web Tracker"
const val MOCKK_TEXT = "Conteúdo principal"

