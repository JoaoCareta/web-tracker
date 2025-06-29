package com.joao.otavio.authentication_domain.repository

import com.joao.otavio.authentication_presentation.datasource.AuthenticationLocalDataSource
import com.joao.otavio.authentication_presentation.datasource.AuthenticationRemoteDataSource
import com.joao.otavio.authentication_presentation.repository.AuthenticationRepository
import com.joao.otavio.authentication_presentation.usecases.SaveOrganizationUseCase
import com.joao.otavio.core.logger.WebTrackerLogger
import com.joao.otavio.core.extensions.string.isNotNullOrEmptyOrBlank
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val saveOrganizationUseCase: SaveOrganizationUseCase,
    private val logger: WebTrackerLogger
) : AuthenticationRepository {
    override suspend fun authenticateUserWithEmailAndPassword(
        userEmail: String,
        userPassword: String
    ): Boolean {
        logger.i(logger.getTag(), "Attempting to authenticate user with email")

        return try {
            val isAuthenticatedRemotely = authenticationRemoteDataSource.authenticateOrganization(
                organizationEmail = userEmail,
                organizationPassword = userPassword
            )

            if (isAuthenticatedRemotely) {
                logger.i(logger.getTag(), "User authenticated successfully with remote service.")
                val authenticatedOrganization = authenticationRemoteDataSource.getLoginOrganization()
                authenticatedOrganization?.let { organization ->
                    logger.i(
                        logger.getTag(),
                        "Successfully retrieved userId after authentication."
                    )
                    saveOrganizationUseCase.invoke(organization)
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
                    "Authentication failed with remote service for user."
                )
                false
            }
        } catch (e: Exception) {
            logger.e(
                logger.getTag(),
                "An error occurred during authentication for user.", e
            )
            false
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        val userId = authenticationLocalDataSource.getOrganizationIdInDataStore()
        val isLoggedIn = userId.isNotNullOrEmptyOrBlank()
        logger.i(
            logger.getTag(),
            "Checking if user is logged in. UserId in DataStore', IsLoggedIn: $isLoggedIn"
        )
        return isLoggedIn
    }
}
