package com.joao.otavio.authentication_presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.joao.otavio.design_system.design.themes.MainTheme
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.dimensions.LocalDimensions
import com.joao.otavio.design_system.scaffold.WebTrackerScaffold
import com.joao.otavio.webtracker.common.desygn.system.R

@Composable
fun DummyScreen(
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current

    WebTrackerScaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Background image
            Image(
                painter = painterResource(id = R.drawable.ic_login_background),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MainTheme().primaryText,
                    modifier = Modifier
                        .padding(top = dimensions.xxxLarge)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    color = MainTheme().primaryText,
                    modifier = Modifier
                        .padding(bottom = dimensions.xLarge)
                )

                Text(
                    text = "Dummy",
                    style = MaterialTheme.typography.displaySmall,
                    color = MainTheme().primaryText,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(
                            end = dimensions.xxxSmall,
                            bottom = dimensions.xxxSmall
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DummyScreenPreview() {
    WebTrackerTheme {
        DummyScreen()
    }
}
