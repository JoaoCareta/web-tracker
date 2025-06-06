package com.joao.otavio.authentication_presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.joao.otavio.authentication_presentation.events.AuthenticationEvents
import com.joao.otavio.authentication_presentation.state.WebTrackerAuthenticationState

abstract class IWebTrackerAuthenticationViewModel: ViewModel() {
    abstract val webTrackerAuthenticationState: WebTrackerAuthenticationState
    abstract fun isOrganizationAlreadyLoggedIn()
    abstract fun onUiEvents(authenticationEvents: AuthenticationEvents)
}
