package com.joao.otavio.authentication_presentation.authentication

import com.joao.otavio.authentication_data.model.domain.Organization

interface Authentication {
    suspend fun loginOrganizationWithEmailAndPassword(userEmail: String, userPassword: String): Boolean
    fun getLoginOrganization(): Organization?
}
