package com.joao.otavio.authentication_presentation.datasource

import com.joao.otavio.authentication_data.model.domain.Organization

interface AuthenticationRemoteDataSource {
    suspend fun authenticateOrganization(organizationEmail: String, organizationPassword: String): Boolean
    fun getLoginOrganization(): Organization?
}
