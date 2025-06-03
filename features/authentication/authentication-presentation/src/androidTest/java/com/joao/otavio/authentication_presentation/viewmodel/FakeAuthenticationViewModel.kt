package com.joao.otavio.authentication_presentation.viewmodel

import androidx.compose.material3.SnackbarDuration
import com.joao.otavio.authentication_presentation.events.AuthenticationEvents
import com.joao.otavio.authentication_presentation.state.AuthenticateState
import com.joao.otavio.authentication_presentation.state.AuthenticationErrorType
import com.joao.otavio.authentication_presentation.state.WebTrackerAuthenticationState
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasImeAction
import kotlinx.coroutines.delay

class FakeAuthenticationViewModel : IWebTrackerAuthenticationViewModel() {
    private val _showLoginFields = MutableStateFlow(false)
    private val _userEmail = MutableStateFlow("")
    private val _userPassword = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(false)
    private val _displayErrorSnackBar = MutableStateFlow(false)
    private val _isAuthenticateSucceed = MutableStateFlow(AuthenticateState.IDLE)
    private val _authenticationErrorType = MutableStateFlow(AuthenticationErrorType.NONE)

    override val webTrackerAuthenticationState: WebTrackerAuthenticationState = WebTrackerAuthenticationState(
        showLoginFields = _showLoginFields,
        userEmail = _userEmail,
        userPassword = _userPassword,
        isLoading = _isLoading,
        displayErrorSnackBar = _displayErrorSnackBar,
        isAuthenticateSucceed = _isAuthenticateSucceed,
        authenticationErrorType = _authenticationErrorType
    )

    override fun isUserAlreadyLoggedIn() {
        _isLoading.value = true
        _isAuthenticateSucceed.value = AuthenticateState.IDLE
        _isLoading.value = false
    }

    override fun onUiEvents(authenticationEvents: AuthenticationEvents) {
        when (authenticationEvents) {
            is AuthenticationEvents.OnTypingEmail -> {
                _userEmail.value = authenticationEvents.newEmailString
            }
            is AuthenticationEvents.OnTypingPassword -> {
                _userPassword.value = authenticationEvents.newPasswordString
            }
            AuthenticationEvents.OnDisplayLoginFieldsClick -> {
                _showLoginFields.value = !_showLoginFields.value
                if (_showLoginFields.value) {
                    _userEmail.value = ""
                    _userPassword.value = ""
                }
            }
            AuthenticationEvents.OnLoginUpClick -> {
                _isLoading.value = true
                when {
                    _userEmail.value.isEmpty() -> {
                        _authenticationErrorType.value = AuthenticationErrorType.EMPTY_EMAIL
                        _displayErrorSnackBar.value = true
                    }
                    !_userEmail.value.contains("@") -> {
                        _authenticationErrorType.value = AuthenticationErrorType.EMAIL_INVALID_FORMAT
                        _displayErrorSnackBar.value = true
                    }
                    _userPassword.value.isEmpty() -> {
                        _authenticationErrorType.value = AuthenticationErrorType.EMPTY_PASSWORD
                        _displayErrorSnackBar.value = true
                    }
                    _userEmail.value == "test@example.com" && _userPassword.value == "password" -> {
                        _isAuthenticateSucceed.value = AuthenticateState.AUTHENTICATE
                    }
                    else -> {
                        _authenticationErrorType.value = AuthenticationErrorType.AUTHENTICATION_FAILED
                        _displayErrorSnackBar.value = true
                    }
                }
                _isLoading.value = false
            }
            AuthenticationEvents.OnSnackBarDismiss -> {
                _displayErrorSnackBar.value = false
                _authenticationErrorType.value = AuthenticationErrorType.NONE
            }
        }
    }

    // Helper methods for testing
    fun setShowLoginFields(show: Boolean) {
        _showLoginFields.value = show
    }

    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    fun setAuthenticationSucceed(state: AuthenticateState) {
        _isAuthenticateSucceed.value = state
    }

    fun setEmail(email: String) {
        _userEmail.value = email
    }

    fun setPassword(password: String) {
        _userPassword.value = password
    }

    fun setDisplayErrorSnackBar(display: Boolean) {
        _displayErrorSnackBar.value = display
    }

    fun setNetworkError() {
        _authenticationErrorType.value = AuthenticationErrorType.NO_INTERNET_CONNECTION
        _displayErrorSnackBar.value = true
    }
}
