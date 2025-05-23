package com.joao.otavio.authentication_presentation.ui.screens

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.joao.otavio.core.navigation.WebTrackerScreens
import com.joao.otavio.core.util.UiEvent
import com.joao.otavio.design_system.buttons.WebTrackerButton
import com.joao.otavio.design_system.design.themes.MainTheme
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.dimensions.LocalDimensions
import com.joao.otavio.design_system.scaffold.WebTrackerScaffold
import com.joao.otavio.webtracker.common.desygn.system.R

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun LoginScreen(
    version: String,
    onEnterClick: (UiEvent.Navigate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val dynamicSpace = remember { (screenHeight * DISTANCE_BETWEEN) }
    val dimensions = LocalDimensions.current
    val signInLauncher = rememberLauncherForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
        if(result.resultCode == RESULT_OK) {
            onEnterClick.invoke(UiEvent.Navigate(WebTrackerScreens.Dummy.route))
        }
    }

    WebTrackerScaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_login_background),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = dimensions.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Spacer(modifier = Modifier.height(dimensions.medium))
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MainTheme().primaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(horizontal = dimensions.xSmall)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(dynamicSpace))
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = dimensions.xSmall,
                                vertical = dimensions.large
                            )
                    ) {
                        WebTrackerButton(
                            text = stringResource(R.string.authentication_login),
                            onClick = {
                                val providers = arrayListOf(
                                    AuthUI.IdpConfig.EmailBuilder().build(),
                                    AuthUI.IdpConfig.GoogleBuilder().build(),
                                )

                                val signInIntent = AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build()
                                signInLauncher.launch(signInIntent)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            theme = MainTheme()
                        )
                    }
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        color = MainTheme().primaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(vertical = dimensions.xSmall)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = version,
                            style = MaterialTheme.typography.displaySmall,
                            color = MainTheme().primaryText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                }
            }
        }
    }
}

const val DISTANCE_BETWEEN = 0.66f

@Preview(showBackground = true)
@Composable
fun LoginScreenMainThemePreview() {
    WebTrackerTheme {
        LoginScreen(
            version = "1.0.8-RC.8",
            onEnterClick = {},
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun LoginScreenDarkThemePreview() {
    WebTrackerTheme {
        LoginScreen(
            version = "1.0.8-RC.8",
            onEnterClick = {},
        )
    }
}
