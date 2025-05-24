package com.joao.otavio.authentication_presentation.events

sealed class AuthenticationEvents {
    data object OnSingUpClick : AuthenticationEvents()
    data object OnLoginUpClick : AuthenticationEvents()
}
