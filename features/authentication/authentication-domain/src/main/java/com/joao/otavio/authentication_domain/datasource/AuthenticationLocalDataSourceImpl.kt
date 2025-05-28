package com.joao.otavio.authentication_domain.datasource

import android.util.Log
import com.joao.otavio.authentication_presentation.datasource.AuthenticationLocalDataSource
import com.joao.otavio.core.datastore.DataStoreKeyConstants.WebTrackerAuthentication
import com.joao.otavio.core.datastore.WebTrackerDataStore
import javax.inject.Inject

class AuthenticationLocalDataSourceImpl @Inject constructor(
    private val webTrackerDataStore: WebTrackerDataStore
): AuthenticationLocalDataSource {
    override suspend fun saveUserIdInDataStore(userId: String): Boolean {
        return try {
            webTrackerDataStore.savePreference(WebTrackerAuthentication.FIREBASE_USER_ID, userId)
            Log.i(TAG, "Successful save userId in Datastore")
            true
        } catch (t: Throwable) {
            Log.e(TAG, "Unexpected error during saving userId", t)
            false
        }
    }

    override suspend fun getUserIdInDataStore(): String? {
        return try {
            Log.i(TAG, "Successful get userId in Datastore")
            webTrackerDataStore.getPreference(WebTrackerAuthentication.FIREBASE_USER_ID)
        } catch (t: Throwable) {
            Log.e(TAG, "Unexpected error during catching userId", t)
            null
        }
    }

    companion object {
        private const val TAG = "Datastore"
    }
}
