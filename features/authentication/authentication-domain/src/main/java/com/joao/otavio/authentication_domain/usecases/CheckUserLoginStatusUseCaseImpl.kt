package com.joao.otavio.authentication_domain.usecases

import com.joao.otavio.authentication_presentation.repository.AuthenticationRepository
import com.joao.otavio.authentication_presentation.usecases.CheckUserLoginStatusUseCase
import com.joao.otavio.core.coroutine.CoroutineContextProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckUserLoginStatusUseCaseImpl @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val coroutineContextProvider: CoroutineContextProvider
): CheckUserLoginStatusUseCase {
    override suspend fun invoke(): Result<Boolean> {
        return withContext(coroutineContextProvider.IO) {
            try {
                val result = authenticationRepository.isUserLoggedIn()
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
