package com.joao.otavio.authentication_presentation.datasource

interface AuthenticationLocalDataSource {
    suspend fun saveUserIdInDataStore(userId: String): Boolean
    suspend fun getUserIdInDataStore(): String?
}
