package com.joao.otavio.authentication_domain.authentication

import com.google.firebase.auth.FirebaseAuth
import com.joao.otavio.authentication_data.model.domain.Organization
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
    override suspend fun loginOrganizationWithEmailAndPassword(
        userEmail: String,
        userPassword: String
    ): Boolean {
        logger.i(logger.getTag(), "Attempting login for organization with email")
        return suspendCancellableCoroutine { continuation ->
            try {
                firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnSuccessListener { _ ->
                        logger.i(logger.getTag(), "Login successful for organization with UID")
                        continuation.resume(true, onCancellation = null)
                    }
                    .addOnFailureListener { exception ->
                        logger.e(
                            logger.getTag(),
                            "Login failed for organization with email",
                            exception
                        )
                        continuation.resume(false, onCancellation = null)
                    }
                    .addOnCanceledListener {
                        logger.w(
                            logger.getTag(),
                            "Login operation cancelled for organization with email"
                        )
                        continuation.resume(false, onCancellation = null)
                    }
            } catch (t: Throwable) {
                logger.e(
                    logger.getTag(),
                    "Unexpected error during login attempt for organization",
                    t
                )
                continuation.resume(false, onCancellation = null)
            }
        }
    }

    override fun getLoginOrganization(): Organization? {
        return try {
            firebaseAuth.currentUser?.let { currentOrganization ->
                logger.i(
                    logger.getTag(),
                    "Successfully retrieved current organization UID"
                )
                Organization(
                    organizationUuid = currentOrganization.uid,
                    organizationName = currentOrganization.displayName ?: DEFAULT_NAME
                )
            } ?: run {
                logger.i(logger.getTag(), "No current organization logged in.")
                null
            }
        } catch (t: Throwable) {
            logger.e(logger.getTag(), "Unexpected error while retrieving current organization ID", t)
            null
        }
    }

    companion object {
        const val DEFAULT_NAME = "no_name_informed"
    }
}
