package com.joao.otavio.authentication_presentation.usecases

fun interface AuthenticateUserUseCase {
    suspend operator fun invoke(email: String, password: String) : Result<Boolean>
}
