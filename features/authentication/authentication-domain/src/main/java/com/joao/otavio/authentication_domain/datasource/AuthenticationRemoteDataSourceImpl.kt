package com.joao.otavio.authentication_domain.datasource

import com.joao.otavio.authentication_data.model.domain.Organization
import com.joao.otavio.authentication_presentation.authentication.Authentication
import com.joao.otavio.authentication_presentation.datasource.AuthenticationRemoteDataSource
import javax.inject.Inject

class AuthenticationRemoteDataSourceImpl @Inject constructor(
    private val authentication: Authentication
): AuthenticationRemoteDataSource {
    override suspend fun authenticateOrganization(organizationEmail: String, organizationPassword: String): Boolean {
        return authentication.loginOrganizationWithEmailAndPassword(
            userEmail = organizationEmail,
            userPassword = organizationPassword
        )
    }

    override fun getLoginOrganization(): Organization? {
        return authentication.getLoginOrganization()
    }
}
