package com.joao.otavio.authentication_domain.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class FirebaseAuthentication @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun loginUserWithEmailAndPassword(userEmail: String, userPassword: String): Boolean {
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

    companion object {
        private const val TAG = "FirebaseAuth"
    }
}
