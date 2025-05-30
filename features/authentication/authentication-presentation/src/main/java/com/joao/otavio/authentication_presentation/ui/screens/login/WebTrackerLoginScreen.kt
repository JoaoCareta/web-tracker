package com.joao.otavio.authentication_presentation.ui.screens.login

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.joao.otavio.authentication_presentation.events.AuthenticationEvents
import com.joao.otavio.authentication_presentation.state.AuthenticateState
import com.joao.otavio.authentication_presentation.state.WebTrackerAuthenticationState
import com.joao.otavio.authentication_presentation.viewmodel.IWebTrackerLoginViewModel
import com.joao.otavio.authentication_presentation.viewmodel.WebTrackerLoginViewModel
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
import com.joao.otavio.design_system.snackbar.WebTrackerSnackBar
import com.joao.otavio.webtracker.common.desygn.system.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest


@Composable
fun LoginScreen(
    version: String,
    onEnterClick: (UiEvent.Navigate) -> Unit,
    modifier: Modifier = Modifier,
    loginViewModel: IWebTrackerLoginViewModel = hiltViewModel<WebTrackerLoginViewModel>()
) {
    val dimensions = LocalDimensions.current
    val alpha = LocalAlpha.current
    val showLoginFields = loginViewModel.webTrackerAuthenticationState.showLoginFields.collectAsState().value
    val email = loginViewModel.webTrackerAuthenticationState.userEmail.collectAsState().value
    val password = loginViewModel.webTrackerAuthenticationState.userPassword.collectAsState().value
    val isLoading = loginViewModel.webTrackerAuthenticationState.isLoading.collectAsState().value
    val buttonText = if (showLoginFields) {
        stringResource(R.string.authentication_login)
    } else {
        stringResource(R.string.authentication_signIn)
    }
    val displaySnackBar = loginViewModel.webTrackerAuthenticationState.displayErrorSnackBar.collectAsState().value

    LaunchedEffect(key1 = true) {
        loginViewModel.webTrackerAuthenticationState.isAuthenticateSucceed.collectLatest { authenticateState ->
            when(authenticateState) {
                AuthenticateState.AUTHENTICATE -> {
                    onEnterClick.invoke(
                        UiEvent.Navigate(
                            route = WebTrackerScreens.Dummy.route,
                            popUpToRoute = WebTrackerScreens.Login.route,
                            inclusive = true
                        )
                    )
                }
                AuthenticateState.ERROR,
                AuthenticateState.IDLE -> Unit
            }
        }
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
            dimensions = dimensions,
            alpha = alpha,
            displaySnackBar = displaySnackBar,
            onEmailChange = { newEmailString ->
                loginViewModel
                    .onUiEvents(AuthenticationEvents.OnTypingEmail(newEmailString = newEmailString))
            },
            onPasswordChange = { newPasswordString ->
                loginViewModel
                    .onUiEvents(AuthenticationEvents.OnTypingPassword(newPasswordString = newPasswordString))
            },
            onButtonClick = {
                if (!showLoginFields) {
                    loginViewModel.onUiEvents(AuthenticationEvents.OnDisplayLoginFieldsClick)
                } else {
                    loginViewModel.onUiEvents(AuthenticationEvents.OnLoginUpClick)
                }
            },
            onCancelClick = {
                loginViewModel.onUiEvents(AuthenticationEvents.OnDisplayLoginFieldsClick)
            },
            onDismissSnackBar = {
                loginViewModel.onUiEvents(AuthenticationEvents.OnSnackBarDismiss)
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
    displaySnackBar: Boolean,
    onDismissSnackBar: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    onCancelClick: () -> Unit,
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
                onCancelClick = onCancelClick,
                dimensions = dimensions,
                displaySnackBar = displaySnackBar,
                onDismissSnackBar = onDismissSnackBar,
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
    displaySnackBar: Boolean,
    onDismissSnackBar: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    onCancelClick: () -> Unit,
    dimensions: Dimensions,
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

        ShowLoginErrorSnackBar(
            displaySnackBar = displaySnackBar,
            dimensions = dimensions,
            onDismissSnackBar = onDismissSnackBar
        )

        LoginFooter(
            showLoginFields = showLoginFields,
            email = email,
            password = password,
            buttonText = buttonText,
            version = version,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onButtonClick = onButtonClick,
            onCancelClick = onCancelClick,
            dimensions = dimensions,
        )
    }
}

@Composable
fun ShowLoginErrorSnackBar(
    displaySnackBar: Boolean,
    onDismissSnackBar: () -> Unit,
    dimensions: Dimensions
) {
    AnimatedVisibility(
        visible = displaySnackBar,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensions.xSmall),
            contentAlignment = Alignment.BottomCenter
        ) {
            WebTrackerSnackBar (
                visible = true,
                title = stringResource(R.string.authentication_login_error),
                iconId = R.drawable.ic_close,
                duration = SnackbarDuration.Long.ordinal,
                onDismiss = onDismissSnackBar,
            )
        }
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
    onCancelClick: () -> Unit,
    dimensions: Dimensions,
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

        if (showLoginFields) {
            CancelText(
                dimensions = dimensions,
                onCancelClick = onCancelClick
            )
        }

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
fun CancelText(
    dimensions: Dimensions,
    onCancelClick: () -> Unit,
) {
    Text(
        text = stringResource(R.string.authentication_cancel),
        style = MaterialTheme.typography.displaySmall.copy(
            textDecoration = TextDecoration.Underline,
            color = MainTheme().secondary
        ),
        modifier = Modifier
            .clickable { onCancelClick.invoke() }
            .padding(vertical = dimensions.xxxSmall)
    )
    Spacer(modifier = Modifier.height(dimensions.xxxSmall))
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
        visualTransformation = PasswordVisualTransformation(),
        useSpacer = false
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

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun LoginScreenMainThemePreview() {
    WebTrackerTheme {
        LoginScreen(
            version = "1.0.8-RC.8",
            onEnterClick = {},
            loginViewModel = WebTrackerViewModelPreview()
        )
    }
}

@SuppressLint("ViewModelConstructorInComposable")
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
            loginViewModel = WebTrackerViewModelPreview()
        )
    }
}

class WebTrackerViewModelPreview : IWebTrackerLoginViewModel() {
    override val webTrackerAuthenticationState: WebTrackerAuthenticationState
        get() = WebTrackerAuthenticationState(
            showLoginFields = MutableStateFlow(true)
        )

    override fun isUserAlreadyLoggedIn() {
        // Do nothing
    }

    override fun onUiEvents(authenticationEvents: AuthenticationEvents) {
        // Do nothing
    }
}
