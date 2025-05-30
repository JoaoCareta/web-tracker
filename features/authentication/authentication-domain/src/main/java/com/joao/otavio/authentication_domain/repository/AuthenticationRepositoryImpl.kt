package com.joao.otavio.authentication_domain.repository

import com.joao.otavio.authentication_presentation.datasource.AuthenticationLocalDataSource
import com.joao.otavio.authentication_presentation.datasource.AuthenticationRemoteDataSource
import com.joao.otavio.authentication_presentation.repository.AuthenticationRepository
import com.joao.otavio.core.logger.WebTrackerLogger
import com.joao.otavio.core.util.isNotNullOrEmptyOrBlank
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val logger: WebTrackerLogger
) : AuthenticationRepository {
    override suspend fun authenticateUserWithEmailAndPassword(
        userEmail: String,
        userPassword: String
    ): Boolean {
        logger.i(logger.getTag(), "Attempting to authenticate user with email: $userEmail")

        return try {
            val isAuthenticatedRemotely = authenticationRemoteDataSource.authenticateUser(
                userEmail = userEmail,
                userPassword = userPassword
            )

            if (isAuthenticatedRemotely) {
                logger.i(logger.getTag(), "User authenticated successfully with remote service.")
                val authenticatedUserId = authenticationRemoteDataSource.getLoginUserId()
                authenticatedUserId?.let { userId ->
                    logger.i(
                        logger.getTag(),
                        "Successfully retrieved userId: $userId after authentication."
                    )
                    authenticationLocalDataSource.saveUserIdInDataStore(userId)
                } ?: run {
                    logger.w(
                        logger.getTag(),
                        "Authentication successful, but failed to retrieve userId from remote service."
                    )
                    false
                }
            } else {
                logger.e(
                    logger.getTag(),
                    "Authentication failed with remote service for user: $userEmail."
                )
                false
            }
        } catch (e: Exception) {
            logger.e(
                logger.getTag(),
                "An error occurred during authentication for user: $userEmail.", e
            )
            false
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        val userId = authenticationLocalDataSource.getUserIdInDataStore()
        val isLoggedIn = userId.isNotNullOrEmptyOrBlank()
        logger.i(
            logger.getTag(),
            "Checking if user is logged in. UserId in DataStore: '$userId', IsLoggedIn: $isLoggedIn"
        )
        return isLoggedIn
    }
}
