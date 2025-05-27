package com.joao.otavio.authentication_presentation.datasource

fun interface AuthenticationRemoteDataSource {
    suspend fun authenticateUser(userEmail: String, userPassword: String): Boolean
}
