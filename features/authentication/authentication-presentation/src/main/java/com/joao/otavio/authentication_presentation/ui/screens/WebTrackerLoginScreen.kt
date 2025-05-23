package com.joao.otavio.authentication_presentation.ui.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.joao.otavio.core.navigation.WebTrackerScreens
import com.joao.otavio.core.util.UiEvent
import com.joao.otavio.design_system.buttons.WebTrackerButton
import com.joao.otavio.design_system.design.themes.MainTheme
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.dimensions.LocalDimensions
import com.joao.otavio.design_system.outlinedTextField.WebTrackerOutlinedTextField
import com.joao.otavio.design_system.scaffold.WebTrackerScaffold
import com.joao.otavio.webtracker.common.desygn.system.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun LoginScreen(
    version: String,
    onEnterClick: (UiEvent.Navigate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    var showLoginFields by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val buttonText = if (showLoginFields) {
        stringResource(R.string.authentication_login)
    } else {
        stringResource(R.string.authentication_signIn)
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

            if (!isLoading) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MainTheme().primaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = dimensions.medium)
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = dimensions.medium)
                        .padding(bottom = dimensions.medium)
                        .imePadding(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedVisibility(
                        visible = showLoginFields,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = dimensions.xSmall)
                        ) {

                            WebTrackerOutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = stringResource(R.string.authentication_email),
                                leadingIcon = Icons.Default.Email,
                                theme = MainTheme(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                )
                            )

                            WebTrackerOutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = stringResource(R.string.authentication_password),
                                leadingIcon = Icons.Default.Lock,
                                theme = MainTheme(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                ),
                                visualTransformation = PasswordVisualTransformation()
                            )
                        }
                    }

                    WebTrackerButton(
                        text = buttonText,
                        onClick = {
                            if (!showLoginFields) {
                                showLoginFields = true
                            } else {
                                scope.launch {
                                    isLoading = true
                                    delay(DELAY_TIME)
                                    isLoading = false
                                    onEnterClick.invoke(UiEvent.Navigate(WebTrackerScreens.Dummy.route))
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensions.xSmall),
                        theme = MainTheme(),
                        enabled = !isLoading
                    )

                    Spacer(modifier = Modifier.height(dimensions.medium))

                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        color = MainTheme().primaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth()
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

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MainTheme().secondary
                    )
                }
            }
        }
    }
}


const val DELAY_TIME = 3000L

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
