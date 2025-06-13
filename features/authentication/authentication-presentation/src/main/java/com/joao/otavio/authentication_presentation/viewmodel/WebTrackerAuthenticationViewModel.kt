package com.joao.otavio.authentication_presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.joao.otavio.authentication_presentation.events.AuthenticationEvents
import com.joao.otavio.authentication_presentation.state.AuthenticateState.AUTHENTICATE
import com.joao.otavio.authentication_presentation.state.AuthenticateState.ERROR
import com.joao.otavio.authentication_presentation.state.AuthenticateState.IDLE
import com.joao.otavio.authentication_presentation.state.AuthenticateState.LOADING
import com.joao.otavio.authentication_presentation.state.AuthenticationErrorType
import com.joao.otavio.authentication_presentation.state.AuthenticationErrorType.ACCOUNT_LOCKED
import com.joao.otavio.authentication_presentation.state.AuthenticationErrorType.AUTHENTICATION_FAILED
import com.joao.otavio.authentication_presentation.state.AuthenticationErrorType.EMAIL_INVALID_FORMAT
import com.joao.otavio.authentication_presentation.state.AuthenticationErrorType.EMPTY_EMAIL
import com.joao.otavio.authentication_presentation.state.AuthenticationErrorType.EMPTY_PASSWORD
import com.joao.otavio.authentication_presentation.state.AuthenticationErrorType.NO_INTERNET_CONNECTION
import com.joao.otavio.authentication_presentation.state.WebTrackerAuthenticationState
import com.joao.otavio.authentication_presentation.usecases.AuthenticateUserUseCase
import com.joao.otavio.authentication_presentation.usecases.CheckOrganizationLoginStatusUseCase
import com.joao.otavio.core.coroutine.CoroutineContextProvider
import com.joao.otavio.core.util.IsNetworkAvailableUseCase
import com.joao.otavio.core.util.TimeUtils.ONE_MINUTE
import com.joao.otavio.core.util.TimeUtils.ONE_SECOND
import com.joao.otavio.core.util.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebTrackerAuthenticationViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    private val checkOrganizationLoginStatusUseCase: CheckOrganizationLoginStatusUseCase,
    private val isNetworkAvailableUseCase: IsNetworkAvailableUseCase,
    private val coroutineContextProvider: CoroutineContextProvider,
) : BaseWebTrackerAuthenticationViewModel() {
    override val webTrackerAuthenticationState: WebTrackerAuthenticationState = WebTrackerAuthenticationState()

    // Lock control variables
    private var consecutiveFailedAttempts = 0
    private var lockoutMultiplier = 0
    private var isLocked = false
    private var lockoutEndTime = 0L
    private val lockoutJob = Job()

    init {
        isOrganizationAlreadyLoggedIn()
    }

    // Public methods
    override fun onUiEvents(authenticationEvents: AuthenticationEvents) {
        when (authenticationEvents) {
            is AuthenticationEvents.OnLoginUpClick -> handleOnLoginUpClick()
            is AuthenticationEvents.OnDisplayLoginFieldsClick -> handleOnDisplayLoginFieldsClick()
            is AuthenticationEvents.OnTypingEmail -> handleOnTypingEmail(authenticationEvents.newEmailString)
            is AuthenticationEvents.OnTypingPassword -> handleOnTypingPassword(authenticationEvents.newPasswordString)
            is AuthenticationEvents.OnSnackBarDismiss -> handleSnackBarDismiss()
            is AuthenticationEvents.OnAuthenticationStateUpdate -> handleAuthenticationStateUpdate()
        }
    }

    override fun isOrganizationAlreadyLoggedIn() {
        if (!isNetworkAvailable()) {
            showError(NO_INTERNET_CONNECTION)
            return
        }
        viewModelScope.launch(coroutineContextProvider.IO) {
            setLoading()
            checkOrganizationLoginStatusUseCase()
                .onSuccess { isLoggedIn ->
                    webTrackerAuthenticationState.authenticateState.update {
                        if (isLoggedIn) AUTHENTICATE else IDLE
                    }
                }
                .onFailure { _ ->
                    showError(AUTHENTICATION_FAILED)
                }
            delay(ONE_SECOND.inWholeMilliseconds)
        }
    }

    // Authentication handling
    private fun handleOnLoginUpClick() {
        if (!isOrganizationAbleToProceedWithAuthentication()) return
        viewModelScope.launch(coroutineContextProvider.IO) {
            setLoading()
            authenticateUserUseCase(
                webTrackerAuthenticationState.organizationEmail.value.trim(),
                webTrackerAuthenticationState.organizationPassword.value
            )
                .onSuccess { isAuthenticated ->
                    handleAuthenticationResult(isAuthenticated = isAuthenticated)
                }
                .onFailure { _ ->
                    showError(AUTHENTICATION_FAILED)
                }
        }
    }

    private fun handleAuthenticationResult(isAuthenticated: Boolean) {
        updateAuthenticateState(isAuthenticated)
        if (isAuthenticated) {
            handleSucceedAuthentication()
        } else {
            handleFailedAuthentication()
        }
    }

    private fun handleSucceedAuthentication() {
        consecutiveFailedAttempts = 0
        lockoutMultiplier = 0
    }

    private fun handleFailedAuthentication() {
        consecutiveFailedAttempts++

        if (consecutiveFailedAttempts >= MAX_ATTEMPTS) {
            lockoutMultiplier++
            consecutiveFailedAttempts = 0
            initiateLoginLockout()
        } else {
            showError(AUTHENTICATION_FAILED)
        }
    }

    // Lockout handling
    private fun initiateLoginLockout() {
        isLocked = true
        val lockoutDuration = BASE_LOCKOUT_DURATION * lockoutMultiplier
        lockoutEndTime = System.currentTimeMillis() + lockoutDuration

        viewModelScope.launch(lockoutJob) {
            try {
                delay(lockoutDuration)
                isLocked = false
            } finally {
                if (!isLocked) {
                    updateRemainingLockoutTime(0L)
                }
            }
        }

        viewModelScope.launch {
            while (isLocked) {
                val remaining = (lockoutEndTime - System.currentTimeMillis()).coerceAtLeast(0)
                updateRemainingLockoutTime(remaining)
                if (remaining <= 0) break
                delay(ONE_SECOND.inWholeMilliseconds)
            }
        }
    }

    // Validation methods
    private fun isOrganizationAbleToProceedWithAuthentication(): Boolean {
        return when {
            isLocked -> {
                showError(ACCOUNT_LOCKED)
                false
            }

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
        val email = webTrackerAuthenticationState.organizationEmail.value.trim()
        val password = webTrackerAuthenticationState.organizationPassword.value

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
        return isNetworkAvailableUseCase.invoke()
    }

    // UI state handling
    private fun handleOnDisplayLoginFieldsClick() {
        webTrackerAuthenticationState.showLoginFields.update { showLoginFields -> !showLoginFields }
        handleOnTypingEmail("")
        handleOnTypingPassword("")
    }

    private fun handleOnTypingEmail(newEmailString: String) {
        webTrackerAuthenticationState.organizationEmail.update { newEmailString }
    }

    private fun handleOnTypingPassword(newPasswordString: String) {
        webTrackerAuthenticationState.organizationPassword.update { newPasswordString }
    }

    private fun handleSnackBarDismiss() {
        webTrackerAuthenticationState.displayErrorSnackBar.update { false }
    }

    private fun handleAuthenticationStateUpdate() {
        webTrackerAuthenticationState.authenticateState.update { IDLE }
    }

    private fun setLoading() {
        webTrackerAuthenticationState.authenticateState.update { LOADING }
    }

    private fun showError(errorType: AuthenticationErrorType) {
        webTrackerAuthenticationState.authenticationErrorType.update { errorType }
        webTrackerAuthenticationState.displayErrorSnackBar.update { true }
    }

    private fun updateAuthenticateState(authenticationSucceed: Boolean) {
        val authenticateState = if (authenticationSucceed) AUTHENTICATE else ERROR
        webTrackerAuthenticationState.authenticateState.update {
            authenticateState
        }
    }

    private fun updateRemainingLockoutTime(remainingTime: Long) {
        webTrackerAuthenticationState.remainingLockoutTime.update { remainingTime }
    }

    companion object {
        const val MAX_ATTEMPTS = 5
        val BASE_LOCKOUT_DURATION = (ONE_MINUTE * 5).inWholeMilliseconds
    }
}
