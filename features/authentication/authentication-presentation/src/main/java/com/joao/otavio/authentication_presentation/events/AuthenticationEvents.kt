package com.joao.otavio.authentication_presentation.events

sealed class AuthenticationEvents {
    data object OnDisplayLoginFieldsClick : AuthenticationEvents()
    data object OnLoginUpClick : AuthenticationEvents()
    data object OnSnackBarDismiss : AuthenticationEvents()
    data class OnTypingEmail(val newEmailString: String) : AuthenticationEvents()
    data class OnTypingPassword(val newPasswordString: String) : AuthenticationEvents()
}
