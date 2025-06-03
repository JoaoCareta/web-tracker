package com.joao.otavio.authentication_presentation.viewmodel

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.viewModelScope
import com.joao.otavio.authentication_presentation.events.AuthenticationEvents
import com.joao.otavio.authentication_presentation.state.AuthenticateState.AUTHENTICATE
import com.joao.otavio.authentication_presentation.state.AuthenticateState.ERROR
import com.joao.otavio.authentication_presentation.state.AuthenticateState.IDLE
import com.joao.otavio.authentication_presentation.state.AuthenticationErrorType.AUTHENTICATION_FAILED
import com.joao.otavio.authentication_presentation.state.AuthenticationErrorType.NO_INTERNET_CONNECTION
import com.joao.otavio.authentication_presentation.state.AuthenticationErrorType.EMPTY_EMAIL
import com.joao.otavio.authentication_presentation.state.AuthenticationErrorType.EMAIL_INVALID_FORMAT
import com.joao.otavio.authentication_presentation.state.AuthenticationErrorType.EMPTY_PASSWORD
import com.joao.otavio.authentication_presentation.state.AuthenticationErrorType
import com.joao.otavio.authentication_presentation.state.WebTrackerAuthenticationState
import com.joao.otavio.authentication_presentation.usecases.AuthenticateUserUseCase
import com.joao.otavio.authentication_presentation.usecases.CheckUserLoginStatusUseCase
import com.joao.otavio.core.coroutine.CoroutineContextProvider
import com.joao.otavio.core.util.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebTrackerAuthenticationViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    private val checkUserLoginStatusUseCase: CheckUserLoginStatusUseCase,
    private val connectivityManager: ConnectivityManager,
    private val coroutineContextProvider: CoroutineContextProvider,
) : IWebTrackerAuthenticationViewModel() {
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
        if (!isNetworkAvailable()) {
            showError(NO_INTERNET_CONNECTION)
            return
        }
        viewModelScope.launch(coroutineContextProvider.IO) {
            setLoading(true)
            checkUserLoginStatusUseCase()
                .onSuccess { isLoggedIn ->
                    webTrackerAuthenticationState.isAuthenticateSucceed.update {
                        if (isLoggedIn) AUTHENTICATE else IDLE
                    }
                }
                .onFailure { _ ->
                    showError(AUTHENTICATION_FAILED)
                }
            delay(ONE_SECOND)
            setLoading(false)
        }
    }

    private fun handleOnLoginUpClick() {
        if (!isUserAbleToProceedWithAuthentication()) return
        viewModelScope.launch(coroutineContextProvider.IO) {
            setLoading(true)
            authenticateUserUseCase(
                webTrackerAuthenticationState.userEmail.value.trim(),
                webTrackerAuthenticationState.userPassword.value
            )
                .onSuccess { isAuthenticated ->
                    handleAuthenticationResult(isAuthenticated)
                }
                .onFailure { _ ->
                    showError(AUTHENTICATION_FAILED)
                }
            setLoading(false)
        }
    }

    private fun handleAuthenticationResult(isAuthenticated: Boolean) {
        webTrackerAuthenticationState.isAuthenticateSucceed.update {
            if (isAuthenticated) AUTHENTICATE else ERROR
        }
        if (!isAuthenticated) {
            showError(AUTHENTICATION_FAILED)
        }
    }

    private fun isUserAbleToProceedWithAuthentication(): Boolean {
        return when {
            !isNetworkAvailable() -> {
                showError(NO_INTERNET_CONNECTION)
                false
            }
            !validateInputs() -> {
                false
            }
            else -> true
        }
    }

    private fun validateInputs(): Boolean {
        val email = webTrackerAuthenticationState.userEmail.value.trim()
        val password = webTrackerAuthenticationState.userPassword.value

        return when {
            email.isEmpty() -> {
                showError(EMPTY_EMAIL)
                false
            }
            !email.isValidEmail() -> {
                showError(EMAIL_INVALID_FORMAT)
                false
            }
            password.isEmpty() -> {
                showError(EMPTY_PASSWORD)
                false
            }
            else -> true
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(
            connectivityManager.activeNetwork
        )
        return networkCapabilities != null &&
            (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    private fun handleOnDisplayLoginFieldsClick() {
        webTrackerAuthenticationState.showLoginFields.update { showLoginFields -> !showLoginFields }
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

    private fun setLoading(isLoading: Boolean) {
        webTrackerAuthenticationState.isLoading.update { isLoading }
    }

    private fun showError(errorType: AuthenticationErrorType) {
        webTrackerAuthenticationState.authenticationErrorType.update { errorType }
        webTrackerAuthenticationState.displayErrorSnackBar.update { true }
    }

    companion object {
        const val ONE_SECOND = 1000L
    }
}
