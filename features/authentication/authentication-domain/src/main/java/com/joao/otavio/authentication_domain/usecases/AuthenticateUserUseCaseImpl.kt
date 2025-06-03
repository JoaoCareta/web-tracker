package com.joao.otavio.authentication_domain.usecases

import com.joao.otavio.authentication_presentation.repository.AuthenticationRepository
import com.joao.otavio.authentication_presentation.usecases.AuthenticateUserUseCase
import com.joao.otavio.core.coroutine.CoroutineContextProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticateUserUseCaseImpl @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val coroutineContextProvider: CoroutineContextProvider
) : AuthenticateUserUseCase {
    override suspend fun invoke(email: String, password: String): Result<Boolean> {
        return withContext(coroutineContextProvider.IO) {
            try {
                val result =
                    authenticationRepository.authenticateUserWithEmailAndPassword(email, password)
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
