package com.joao.otavio.authentication_presentation.ui.screens.login

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.joao.otavio.design_system.dimensions.Alpha
import com.joao.otavio.design_system.dimensions.Dimensions
import com.joao.otavio.design_system.dimensions.LocalAlpha
import com.joao.otavio.design_system.dimensions.LocalDimensions
import com.joao.otavio.design_system.outlinedTextField.WebTrackerOutlinedTextField
import com.joao.otavio.design_system.scaffold.WebTrackerScaffold
import com.joao.otavio.webtracker.common.desygn.system.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val DELAY_TIME = 3000L

@Composable
fun LoginScreen(
    version: String,
    onEnterClick: (UiEvent.Navigate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val alpha = LocalAlpha.current
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
        LoginContent(
            paddingValues = paddingValues,
            isLoading = isLoading,
            showLoginFields = showLoginFields,
            email = email,
            password = password,
            buttonText = buttonText,
            version = version,
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
            dimensions = dimensions,
            alpha = alpha,
            onButtonClick = {
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
            }
        )
    }
}

@Composable
private fun LoginContent(
    paddingValues: PaddingValues,
    isLoading: Boolean,
    showLoginFields: Boolean,
    email: String,
    password: String,
    buttonText: String,
    version: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    dimensions: Dimensions,
    alpha: Alpha
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        LoginBackground()

        if (!isLoading) {
            LoginMainContent(
                showLoginFields = showLoginFields,
                email = email,
                password = password,
                buttonText = buttonText,
                version = version,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onButtonClick = onButtonClick,
                dimensions = dimensions
            )
        }

        if (isLoading) {
            LoadingOverlay(
                alpha = alpha
            )
        }
    }
}

@Composable
private fun LoginBackground() {
    Image(
        painter = painterResource(id = R.drawable.ic_login_background),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun LoginMainContent(
    showLoginFields: Boolean,
    email: String,
    password: String,
    buttonText: String,
    version: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    dimensions: Dimensions
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        LoginHeader(
            Modifier
                .padding(top = dimensions.medium)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.weight(1f))

        LoginFooter(
            showLoginFields = showLoginFields,
            email = email,
            password = password,
            buttonText = buttonText,
            version = version,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onButtonClick = onButtonClick,
            dimensions = dimensions
        )
    }
}

@Composable
private fun LoginHeader(
    modifier: Modifier
) {
    Text(
        text = stringResource(R.string.app_name),
        style = MaterialTheme.typography.headlineLarge,
        color = MainTheme().primaryText,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
private fun LoginFooter(
    showLoginFields: Boolean,
    email: String,
    password: String,
    buttonText: String,
    version: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    dimensions: Dimensions
) {

    Column(
        modifier = Modifier
            .padding(horizontal = dimensions.medium)
            .padding(bottom = dimensions.medium)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginFields(
            showLoginFields = showLoginFields,
            email = email,
            password = password,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            dimensions = dimensions
        )

        LoginButton(
            text = buttonText,
            onClick = onButtonClick,
            dimensions = dimensions
        )

        VersionInfo(
            version = version,
            dimensions = dimensions
        )
    }
}

@Composable
private fun LoginFields(
    showLoginFields: Boolean,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    dimensions: Dimensions
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

            LoginEmailField(
                email = email,
                onEmailChange = onEmailChange
            )

            LoginPasswordField(
                password = password,
                onPasswordChange = onPasswordChange
            )
        }
    }
}

@Composable
fun LoginEmailField(
    email: String,
    onEmailChange: (String) -> Unit
) {
    WebTrackerOutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = stringResource(R.string.authentication_email),
        leadingIcon = Icons.Default.Email,
        theme = MainTheme(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
fun LoginPasswordField(
    password: String,
    onPasswordChange: (String) -> Unit
) {
    WebTrackerOutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
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

@Composable
private fun LoginButton(
    text: String,
    onClick: () -> Unit,
    dimensions: Dimensions
) {
    WebTrackerButton(
        text = text,
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.xSmall),
        theme = MainTheme(),
        enabled = true
    )
}

@Composable
private fun VersionInfo(
    version: String,
    dimensions: Dimensions
) {
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

@Composable
private fun LoadingOverlay(
    alpha: Alpha
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = alpha.ultraHigh)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MainTheme().secondary
        )
    }
}

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
