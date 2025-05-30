package com.joao.otavio.authentication_presentation.state

import kotlinx.coroutines.flow.MutableStateFlow
import com.joao.otavio.authentication_presentation.state.AuthenticateState.IDLE

data class WebTrackerAuthenticationState(
    val userEmail: MutableStateFlow<String> = MutableStateFlow(""),
    val userPassword: MutableStateFlow<String> = MutableStateFlow(""),
    val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val showLoginFields: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val displayErrorSnackBar: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val isAuthenticateSucceed: MutableStateFlow<AuthenticateState> = MutableStateFlow(IDLE)
)

enum class AuthenticateState {
    IDLE, AUTHENTICATE, ERROR
}
