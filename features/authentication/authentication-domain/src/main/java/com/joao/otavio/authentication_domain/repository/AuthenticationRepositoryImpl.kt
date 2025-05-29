package com.joao.otavio.authentication_domain.repository

import android.util.Log
import com.joao.otavio.authentication_presentation.datasource.AuthenticationLocalDataSource
import com.joao.otavio.authentication_presentation.datasource.AuthenticationRemoteDataSource
import com.joao.otavio.authentication_presentation.repository.AuthenticationRepository
import com.joao.otavio.core.util.isNotNullOrEmptyOrBlank
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource
) : AuthenticationRepository {
    override suspend fun authenticateUserWithEmailAndPassword(
        userEmail: String,
        userPassword: String
    ): Boolean {
        val authenticateUser = authenticationRemoteDataSource.authenticateUser(
            userEmail = userEmail,
            userPassword = userPassword
        )

        return if (authenticateUser) {
            val authenticatedUserId = authenticationRemoteDataSource.getLoginUserId()
            authenticatedUserId?.let { userId ->
                Log.i(TAG, "Successfully login the user. userId: $userId")
                authenticationLocalDataSource.saveUserIdInDataStore(userId)
            } ?: run {
                Log.w(TAG, "Error while login the user. Could not get the userId")
                false
            }
        } else {
            Log.e(TAG, "Error while login the user.")
            false
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return authenticationLocalDataSource.getUserIdInDataStore().isNotNullOrEmptyOrBlank()
    }

    companion object {
        const val TAG = "Authentication Repository"
    }
}
