package com.joao.otavio.authentication_presentation.authentication

interface Authentication {
    suspend fun loginUserWithEmailAndPassword(userEmail: String, userPassword: String): Boolean
    fun getLoginUserId(): String?
}
