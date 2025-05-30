package com.joao.otavio.authentication_presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.joao.otavio.authentication_presentation.events.AuthenticationEvents
import com.joao.otavio.authentication_presentation.repository.AuthenticationRepository
import com.joao.otavio.authentication_presentation.state.AuthenticateState.AUTHENTICATE
import com.joao.otavio.authentication_presentation.state.AuthenticateState.ERROR
import com.joao.otavio.authentication_presentation.state.AuthenticateState.IDLE
import com.joao.otavio.authentication_presentation.state.WebTrackerAuthenticationState
import com.joao.otavio.core.coroutine.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebTrackerLoginViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
) : IWebTrackerLoginViewModel() {
    override val webTrackerAuthenticationState: WebTrackerAuthenticationState =
        WebTrackerAuthenticationState()

    init {
        isUserAlreadyLoggedIn()
    }

    override fun onUiEvents(authenticationEvents: AuthenticationEvents) {
        when (authenticationEvents) {
            is AuthenticationEvents.OnLoginUpClick -> handleOnLoginUpClick()
            is AuthenticationEvents.OnDisplayLoginFieldsClick -> handleOnDisplayLoginFieldsClick()
            is AuthenticationEvents.OnTypingEmail -> handleOnTypingEmail(authenticationEvents.newEmailString)
            is AuthenticationEvents.OnTypingPassword -> handleOnTypingPassword(authenticationEvents.newPasswordString)
            is AuthenticationEvents.OnSnackBarDismiss -> handleSnackBarDismiss()
        }
    }

    override fun isUserAlreadyLoggedIn() {
        webTrackerAuthenticationState.isLoading.update { true }
        webTrackerAuthenticationState.isAuthenticateSucceed.update { IDLE }
        viewModelScope.launch(coroutineContextProvider.IO) {
            val authenticationResult = authenticationRepository.isUserLoggedIn()
            val isUserAuthenticated = if (authenticationResult) AUTHENTICATE else IDLE
            webTrackerAuthenticationState.isAuthenticateSucceed.update { isUserAuthenticated }
            delay(ONE_SECOND)
            webTrackerAuthenticationState.isLoading.update { false }
        }
    }

    private fun handleOnLoginUpClick() {
        webTrackerAuthenticationState.isLoading.update { true }
        webTrackerAuthenticationState.isAuthenticateSucceed.update { IDLE }
        viewModelScope.launch(coroutineContextProvider.IO) {
            val authenticationResult = authenticationRepository.authenticateUserWithEmailAndPassword(
                webTrackerAuthenticationState.userEmail.value.trim(),
                webTrackerAuthenticationState.userPassword.value.trim()
            )
            val isUserAuthenticated = if (authenticationResult) AUTHENTICATE else ERROR
            webTrackerAuthenticationState.isAuthenticateSucceed.update { isUserAuthenticated }
            val errorSnackBar = isUserAuthenticated == ERROR
            webTrackerAuthenticationState.displayErrorSnackBar.update { errorSnackBar }
            webTrackerAuthenticationState.isLoading.update { false }
        }
    }

    private fun handleOnDisplayLoginFieldsClick() {
        webTrackerAuthenticationState.showLoginFields.update { !it }
        handleOnTypingEmail("")
        handleOnTypingPassword("")
    }

    private fun handleOnTypingEmail(newEmailString: String) {
        webTrackerAuthenticationState.userEmail.update { newEmailString }
    }

    private fun handleOnTypingPassword(newPasswordString: String) {
        webTrackerAuthenticationState.userPassword.update { newPasswordString }
    }

    private fun handleSnackBarDismiss() {
        webTrackerAuthenticationState.displayErrorSnackBar.update { false }
    }

    companion object {
        const val ONE_SECOND = 1000L
    }
}
