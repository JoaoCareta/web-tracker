package com.joao.otavio.authentication_presentation.datasource

interface AuthenticationRemoteDataSource {
    suspend fun authenticateUser(userEmail: String, userPassword: String): Boolean
    fun getLoginUserId(): String?
}
