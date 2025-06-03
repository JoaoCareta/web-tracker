package com.joao.otavio.authentication_presentation.usecases

fun interface CheckUserLoginStatusUseCase {
    suspend operator fun invoke() : Result<Boolean>
}
