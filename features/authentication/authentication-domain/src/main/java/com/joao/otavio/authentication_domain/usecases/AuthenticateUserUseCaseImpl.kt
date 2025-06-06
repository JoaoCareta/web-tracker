package com.joao.otavio.authentication_domain.usecases

import com.joao.otavio.authentication_presentation.repository.AuthenticationRepository
import com.joao.otavio.authentication_presentation.usecases.AuthenticateUserUseCase
import javax.inject.Inject

class AuthenticateUserUseCaseImpl @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) : AuthenticateUserUseCase {
    override suspend fun invoke(email: String, password: String): Result<Boolean> {
        return try {
            val result =
                authenticationRepository.authenticateUserWithEmailAndPassword(email, password)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
