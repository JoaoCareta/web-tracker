package com.joao.otavio.authentication_domain.authentication

import com.google.firebase.auth.FirebaseAuth
import com.joao.otavio.authentication_presentation.authentication.Authentication
import com.joao.otavio.core.logger.WebTrackerLogger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class FirebaseAuthentication @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val logger: WebTrackerLogger
) : Authentication {
    override suspend fun loginUserWithEmailAndPassword(
        userEmail: String,
        userPassword: String
    ): Boolean {
        logger.i(logger.getTag(), "Attempting login for user with email")
        return suspendCancellableCoroutine { continuation ->
            try {
                firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnSuccessListener { _ ->
                        logger.i(logger.getTag(), "Login successful for user with UID")
                        continuation.resume(true, onCancellation = null)
                    }
                    .addOnFailureListener { exception ->
                        logger.e(
                            logger.getTag(),
                            "Login failed for user with email",
                            exception
                        )
                        continuation.resume(false, onCancellation = null)
                    }
                    .addOnCanceledListener {
                        logger.w(
                            logger.getTag(),
                            "Login operation cancelled for user with email"
                        )
                        continuation.resume(false, onCancellation = null)
                    }
            } catch (t: Throwable) {
                logger.e(
                    logger.getTag(),
                    "Unexpected error during login attempt for user",
                    t
                )
                continuation.resume(false, onCancellation = null)
            }
        }
    }

    override fun getLoginUserId(): String? {
        return try {
            firebaseAuth.currentUser?.let { currentUser ->
                logger.i(
                    logger.getTag(),
                    "Successfully retrieved current user UID"
                )
                currentUser.uid
            } ?: run {
                logger.i(logger.getTag(), "No current user logged in.")
                null
            }
        } catch (t: Throwable) {
            logger.e(logger.getTag(), "Unexpected error while retrieving current user ID", t)
            null
        }
    }
}
