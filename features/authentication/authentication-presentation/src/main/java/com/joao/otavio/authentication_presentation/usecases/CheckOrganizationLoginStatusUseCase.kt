package com.joao.otavio.authentication_presentation.usecases

fun interface CheckOrganizationLoginStatusUseCase {
    suspend operator fun invoke() : Result<Boolean>
}
