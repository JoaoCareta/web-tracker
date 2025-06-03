package com.joao.otavio.authentication_domain.datasource

import com.joao.otavio.authentication_presentation.datasource.AuthenticationLocalDataSource
import com.joao.otavio.core.datastore.DataStoreKeyConstants.WebTrackerAuthentication
import com.joao.otavio.core.datastore.WebTrackerDataStore
import com.joao.otavio.core.logger.WebTrackerLogger
import javax.inject.Inject

class AuthenticationLocalDataSourceImpl @Inject constructor(
    private val webTrackerDataStore: WebTrackerDataStore,
    private val logger: WebTrackerLogger
): AuthenticationLocalDataSource {
    override suspend fun saveUserIdInDataStore(userId: String): Boolean {
        return try {
            webTrackerDataStore.savePreference(WebTrackerAuthentication.FIREBASE_USER_ID, userId)
            logger.i(logger.getTag(), "Successful save userId in Datastore")
            true
        } catch (t: Throwable) {
            logger.e(logger.getTag(), "Unexpected error during saving userId", t)
            false
        }
    }

    override suspend fun getUserIdInDataStore(): String? {
        return try {
            logger.i(logger.getTag(), "Successful get userId in Datastore")
            webTrackerDataStore.getPreference(WebTrackerAuthentication.FIREBASE_USER_ID)
        } catch (t: Throwable) {
            logger.e(logger.getTag(), "Unexpected error during catching userId", t)
            null
        }
    }
}
