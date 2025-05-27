package com.joao.otavio.authentication_domain.authentication

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.joao.otavio.authentication_presentation.authentication.Authentication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class FirebaseAuthentication @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : Authentication {
    override suspend fun loginUserWithEmailAndPassword(userEmail: String, userPassword: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            try {
                firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnSuccessListener { authResult ->
                        Log.i(TAG, "Login successful: User ${authResult.user?.uid}")
                        continuation.resume(true, onCancellation = null)
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Login failed", exception)
                        continuation.resume(false, onCancellation = null)
                    }
                    .addOnCanceledListener {
                        Log.w(TAG, "Login operation cancelled")
                        continuation.resume(false, onCancellation = null)
                    }
            } catch (t: Throwable) {
                Log.e(TAG, "Unexpected error during login", t)
                continuation.resume(false, onCancellation = null)
            }
        }
    }

    override fun getLoginUserId(): String? {
        return try {
            val userId = firebaseAuth.currentUser?.uid
            Log.i(TAG, "Get userId successfully: User $userId")
            userId
        } catch (t: Throwable) {
            Log.e(TAG, "Unexpected error during catching userId", t)
            null
        }
    }

    companion object {
        private const val TAG = "FirebaseAuth"
    }
}
