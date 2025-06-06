package com.joao.otavio.authentication_presentation.state

import kotlinx.coroutines.flow.MutableStateFlow
import com.joao.otavio.authentication_presentation.state.AuthenticateState.IDLE
import com.joao.otavio.authentication_presentation.state.AuthenticationErrorType.NONE

data class WebTrackerAuthenticationState(
    val organizationEmail: MutableStateFlow<String> = MutableStateFlow(""),
    val organizationPassword: MutableStateFlow<String> = MutableStateFlow(""),
    val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val showLoginFields: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val displayErrorSnackBar: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val isAuthenticateSucceed: MutableStateFlow<AuthenticateState> = MutableStateFlow(IDLE),
    val authenticationErrorType: MutableStateFlow<AuthenticationErrorType> = MutableStateFlow(NONE),
    val remainingLockoutTime: MutableStateFlow<Long> = MutableStateFlow(0L)
)

enum class AuthenticateState {
    IDLE, AUTHENTICATE, ERROR
}

enum class AuthenticationErrorType {
    AUTHENTICATION_FAILED,
    NO_INTERNET_CONNECTION,
    EMPTY_EMAIL,
    EMAIL_INVALID_FORMAT,
    EMPTY_PASSWORD,
    ACCOUNT_LOCKED,
    NONE
}
