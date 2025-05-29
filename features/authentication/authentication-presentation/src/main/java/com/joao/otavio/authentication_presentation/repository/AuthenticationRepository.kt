package com.joao.otavio.authentication_presentation.repository

interface AuthenticationRepository {
    suspend fun authenticateUserWithEmailAndPassword(userEmail: String, userPassword: String) : Boolean
    suspend fun isUserLoggedIn() : Boolean
}
