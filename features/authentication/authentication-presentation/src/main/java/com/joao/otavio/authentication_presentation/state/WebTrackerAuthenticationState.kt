package com.joao.otavio.authentication_presentation.state

data class WebTrackerAuthenticationState(
    var userEmail: String = "",
    var userPassword: String = "",
    var isLoading: Boolean = false
)
