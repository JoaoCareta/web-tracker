package com.joao.otavio.authentication_domain.usecases

import com.joao.otavio.authentication_presentation.repository.AuthenticationRepository
import com.joao.otavio.authentication_presentation.usecases.CheckOrganizationLoginStatusUseCase
import javax.inject.Inject

class CheckOrganizationLoginStatusUseCaseImpl @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
): CheckOrganizationLoginStatusUseCase {
    override suspend fun invoke(): Result<Boolean> {
        return try {
            val result = authenticationRepository.isUserLoggedIn()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
