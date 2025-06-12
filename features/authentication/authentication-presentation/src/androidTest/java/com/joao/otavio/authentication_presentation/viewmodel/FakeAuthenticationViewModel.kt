package com.joao.otavio.authentication_presentation.viewmodel

import com.joao.otavio.authentication_presentation.events.AuthenticationEvents
import com.joao.otavio.authentication_presentation.state.AuthenticateState
import com.joao.otavio.authentication_presentation.state.AuthenticationErrorType
import com.joao.otavio.authentication_presentation.state.WebTrackerAuthenticationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class FakeAuthenticationViewModel : BaseWebTrackerAuthenticationViewModel() {
    private val _showLoginFields = MutableStateFlow(false)
    private val _userEmail = MutableStateFlow("")
    private val _userPassword = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(false)
    private val _displayErrorSnackBar = MutableStateFlow(false)
    private val _isAuthenticateSucceed = MutableStateFlow(AuthenticateState.IDLE)
    private val _authenticationErrorType = MutableStateFlow(AuthenticationErrorType.NONE)
    private val _remainingLockoutTime = MutableStateFlow(0L)

    override val webTrackerAuthenticationState: WebTrackerAuthenticationState = WebTrackerAuthenticationState(
        showLoginFields = _showLoginFields,
        organizationEmail = _userEmail,
        organizationPassword = _userPassword,
        displayErrorSnackBar = _displayErrorSnackBar,
        authenticateState = _isAuthenticateSucceed,
        authenticationErrorType = _authenticationErrorType,
        remainingLockoutTime = _remainingLockoutTime
    )

    override fun isOrganizationAlreadyLoggedIn() {
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
            is AuthenticationEvents.OnDisplayLoginFieldsClick -> {
                _showLoginFields.value = !_showLoginFields.value
                if (_showLoginFields.value) {
                    _userEmail.value = ""
                    _userPassword.value = ""
                }
            }
            is AuthenticationEvents.OnLoginUpClick -> {
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
                    _userEmail.value == VALID_EMAIL && _userPassword.value == VALID_PASSWORD -> {
                        _isAuthenticateSucceed.value = AuthenticateState.AUTHENTICATE
                    }
                    _userEmail.value == LOCKUP_EMAIL && _userPassword.value == WRONG_PASSWORD -> {
                        _isAuthenticateSucceed.value = AuthenticateState.ERROR
                        _authenticationErrorType.value = AuthenticationErrorType.ACCOUNT_LOCKED
                        _remainingLockoutTime.value = 5.0.toDuration(DurationUnit.MINUTES).inWholeMilliseconds
                        _displayErrorSnackBar.value = true
                    }
                    else -> {
                        _authenticationErrorType.value = AuthenticationErrorType.AUTHENTICATION_FAILED
                        _displayErrorSnackBar.value = true
                    }
                }
                _isLoading.value = false
            }
            is AuthenticationEvents.OnSnackBarDismiss -> {
                _displayErrorSnackBar.value = false
                _authenticationErrorType.value = AuthenticationErrorType.NONE
            }

            is AuthenticationEvents.OnAuthenticationStateUpdate -> {
                _isAuthenticateSucceed.value = AuthenticateState.IDLE
            }
        }
    }

    // Helper methods for testing
    fun setShowLoginFields(show: Boolean) {
        _showLoginFields.value = show
    }

    fun setLoading(loading: Boolean) {
        _isAuthenticateSucceed.value = AuthenticateState.LOADING
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

    companion object {
        const val VALID_EMAIL = "test@example.com"
        const val VALID_PASSWORD = "password"
        const val LOCKUP_EMAIL = "lockup@exemple.com"
        const val WRONG_PASSWORD = "wrong"
    }
}
