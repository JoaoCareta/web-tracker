package com.joao.otavio.authentication_domain.datasource

import com.joao.otavio.authentication_presentation.authentication.Authentication
import com.joao.otavio.authentication_presentation.datasource.AuthenticationRemoteDataSource
import javax.inject.Inject

class AuthenticationRemoteDataSourceImpl @Inject constructor(
    private val authentication: Authentication
): AuthenticationRemoteDataSource {
    override suspend fun authenticateUser(userEmail: String, userPassword: String): Boolean {
        return authentication.loginUserWithEmailAndPassword(
            userEmail = userEmail,
            userPassword = userPassword
        )
    }

    override fun getLoginUserId(): String? {
        return authentication.getLoginUserId()
    }
}
