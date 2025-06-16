package com.joao.otavio.authentication_presentation.ui.screens.authentication

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
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
import com.joao.otavio.authentication_presentation.state.AuthenticationErrorType
import com.joao.otavio.authentication_presentation.state.WebTrackerAuthenticationState
import com.joao.otavio.authentication_presentation.viewmodel.BaseWebTrackerAuthenticationViewModel
import com.joao.otavio.authentication_presentation.viewmodel.WebTrackerAuthenticationViewModel
import com.joao.otavio.core.navigation.WebTrackerScreens
import com.joao.otavio.core.util.NavigationEvent
import com.joao.otavio.core.util.formatToMinutesAndSeconds
import com.joao.otavio.design_system.animationScreen.SuccessAnimationScreen
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


@Composable
fun AuthenticationScreen(
    version: String,
    onEnterClick: (NavigationEvent.Navigate) -> Unit,
    modifier: Modifier = Modifier,
    authenticationViewModel: BaseWebTrackerAuthenticationViewModel = hiltViewModel<WebTrackerAuthenticationViewModel>()
) {
    val dimensions = LocalDimensions.current
    val alpha = LocalAlpha.current
    val showLoginFields =
        authenticationViewModel.webTrackerAuthenticationState.showLoginFields.collectAsState().value
    val email =
        authenticationViewModel.webTrackerAuthenticationState.organizationEmail.collectAsState().value
    val password =
        authenticationViewModel.webTrackerAuthenticationState.organizationPassword.collectAsState().value
    val buttonText = if (showLoginFields) {
        stringResource(R.string.authentication_login)
    } else {
        stringResource(R.string.authentication_signIn)
    }
    val displaySnackBar =
        authenticationViewModel.webTrackerAuthenticationState.displayErrorSnackBar.collectAsState().value
    val errorType =
        authenticationViewModel.webTrackerAuthenticationState.authenticationErrorType.collectAsState().value
    val remainingTime =
        authenticationViewModel.webTrackerAuthenticationState.remainingLockoutTime.collectAsState().value

    val errorMessage = getErrorStringByType(errorType = errorType, remainingTime = remainingTime)

    val authenticateState =
        authenticationViewModel.webTrackerAuthenticationState.authenticateState.collectAsState().value

    when (authenticateState) {
        AuthenticateState.ERROR -> {
            SuccessAnimationScreen(
                animationResId = R.raw.anim_error,
                animatedText = stringResource(R.string.commons_error),
                animatedTextColor = WebTrackerTheme.error,
                onAnimationEnd = {
                    authenticationViewModel.onUiEvents(AuthenticationEvents.OnAuthenticationStateUpdate)
                }
            )
        }
        AuthenticateState.IDLE -> {
            WebTrackerScaffold(
                modifier = modifier.fillMaxSize(),
                containerColor = Color.White
            ) { paddingValues ->
                LoginContent(
                    paddingValues = paddingValues,
                    showLoginFields = showLoginFields,
                    email = email,
                    password = password,
                    buttonText = buttonText,
                    version = version,
                    dimensions = dimensions,
                    displaySnackBar = displaySnackBar,
                    errorMessage = errorMessage,
                    onEmailChange = { newEmailString ->
                        authenticationViewModel
                            .onUiEvents(AuthenticationEvents.OnTypingEmail(newEmailString = newEmailString))
                    },
                    onPasswordChange = { newPasswordString ->
                        authenticationViewModel
                            .onUiEvents(AuthenticationEvents.OnTypingPassword(newPasswordString = newPasswordString))
                    },
                    onButtonClick = {
                        if (!showLoginFields) {
                            authenticationViewModel.onUiEvents(AuthenticationEvents.OnDisplayLoginFieldsClick)
                        } else {
                            authenticationViewModel.onUiEvents(AuthenticationEvents.OnLoginUpClick)
                        }
                    },
                    onCancelClick = {
                        authenticationViewModel.onUiEvents(AuthenticationEvents.OnDisplayLoginFieldsClick)
                    },
                    onDismissSnackBar = {
                        authenticationViewModel.onUiEvents(AuthenticationEvents.OnSnackBarDismiss)
                    }
                )
            }
        }
        AuthenticateState.AUTHENTICATE -> {
            SuccessAnimationScreen(
                animationResId = R.raw.anim_check,
                animatedText = stringResource(R.string.commons_done),
                animatedTextColor = WebTrackerTheme.secondary,
                onAnimationEnd = {
                    onEnterClick.invoke(
                        WebTrackerScreens.EmployeeIdentification.navigateReplacingStack()
                    )
                }
            )
        }
        AuthenticateState.LOADING -> {
            LoadingOverlay(
                alpha = alpha
            )
        }
    }
}

@Composable
private fun LoginContent(
    paddingValues: PaddingValues,
    showLoginFields: Boolean,
    email: String,
    password: String,
    buttonText: String,
    version: String,
    displaySnackBar: Boolean,
    errorMessage: String,
    onDismissSnackBar: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    onCancelClick: () -> Unit,
    dimensions: Dimensions,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        LoginBackground()

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
            errorMessage = errorMessage,
            onDismissSnackBar = onDismissSnackBar,
        )
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
    errorMessage: String,
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
            displaySnackBar = displaySnackBar,
            errorMessage = errorMessage,
            onDismissSnackBar = onDismissSnackBar,
            dimensions = dimensions,
        )
    }
}

@Composable
fun ShowLoginErrorSnackBar(
    displaySnackBar: Boolean,
    errorMessage: String,
    onDismissSnackBar: () -> Unit,
) {
    AnimatedVisibility(
        visible = displaySnackBar,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(stringResource(R.string.authentication_error_snack_bar_tag_test)),
            contentAlignment = Alignment.BottomCenter
        ) {
            WebTrackerSnackBar(
                visible = true,
                title = errorMessage,
                iconId = R.drawable.ic_close,
                duration = SnackbarDuration.Short.ordinal,
                onDismiss = onDismissSnackBar,
                backgroundColor = WebTrackerTheme.error,
                textColor = MainTheme().primary,
                iconColor = MainTheme().primary,
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
            .testTag(stringResource(R.string.app_name_footer_tag_test)),
    )
}

@Composable
private fun LoginFooter(
    showLoginFields: Boolean,
    email: String,
    password: String,
    buttonText: String,
    version: String,
    displaySnackBar: Boolean,
    errorMessage: String,
    onDismissSnackBar: () -> Unit,
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

        ShowLoginErrorSnackBar(
            displaySnackBar = displaySnackBar,
            errorMessage = errorMessage,
            onDismissSnackBar = onDismissSnackBar,
        )

        if (!displaySnackBar) {
            VersionInfo(
                version = version,
                dimensions = dimensions
            )
        }
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
        modifier = Modifier.testTag(stringResource(R.string.app_name_header_tag_test))
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
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .testTag(stringResource(R.string.app_version_text_tag_test)),
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
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = MainTheme().secondary,
            modifier = Modifier
                .testTag(stringResource(R.string.authentication_loading_indicator_tag_test))
        )
    }
}

@Composable
private fun getErrorStringByType(errorType: AuthenticationErrorType, remainingTime: Long): String {
    return when (errorType) {
        AuthenticationErrorType.AUTHENTICATION_FAILED -> {
            stringResource(R.string.authentication_login_error)
        }

        AuthenticationErrorType.NO_INTERNET_CONNECTION -> {
            stringResource(R.string.authentication_internet_connection_error)
        }

        AuthenticationErrorType.EMPTY_EMAIL -> {
            stringResource(R.string.authentication_empty_email_error)
        }

        AuthenticationErrorType.EMAIL_INVALID_FORMAT -> {
            stringResource(R.string.authentication_wrong_email_format_error)
        }

        AuthenticationErrorType.EMPTY_PASSWORD -> {
            stringResource(R.string.authentication_empty_password_error)
        }

        AuthenticationErrorType.ACCOUNT_LOCKED -> {
            stringResource(
                id = R.string.login_locked_message,
                remainingTime.formatToMinutesAndSeconds()
            )
        }

        AuthenticationErrorType.NONE -> ""
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun LoginScreenMainThemePreview() {
    WebTrackerTheme {
        AuthenticationScreen(
            version = "1.0.8-RC.8",
            onEnterClick = {},
            authenticationViewModel = WebTrackerViewModelPreview()
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
        AuthenticationScreen(
            version = "1.0.8-RC.8",
            onEnterClick = {},
            authenticationViewModel = WebTrackerViewModelPreview()
        )
    }
}

class WebTrackerViewModelPreview : BaseWebTrackerAuthenticationViewModel() {
    override val webTrackerAuthenticationState: WebTrackerAuthenticationState
        get() = WebTrackerAuthenticationState(
            showLoginFields = MutableStateFlow(true)
        )

    override fun isOrganizationAlreadyLoggedIn() {
        // Do nothing
    }

    override fun onUiEvents(authenticationEvents: AuthenticationEvents) {
        // Do nothing
    }
}
