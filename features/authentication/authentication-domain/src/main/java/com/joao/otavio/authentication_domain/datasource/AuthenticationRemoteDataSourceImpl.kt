package com.joao.otavio.authentication_domain.datasource

import com.joao.otavio.authentication_domain.firebase.FirebaseAuthentication
import com.joao.otavio.authentication_presentation.datasource.AuthenticationRemoteDataSource
import javax.inject.Inject

class AuthenticationRemoteDataSourceImpl @Inject constructor(
    private val firebaseAuthentication: FirebaseAuthentication
): AuthenticationRemoteDataSource {
    override suspend fun authenticateUser(userEmail: String, userPassword: String): Boolean {
        return firebaseAuthentication.loginUserWithEmailAndPassword(
            userEmail = userEmail,
            userPassword = userPassword
        )
    }
}
