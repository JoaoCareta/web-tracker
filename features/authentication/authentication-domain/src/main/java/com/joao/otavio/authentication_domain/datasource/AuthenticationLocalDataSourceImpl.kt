package com.joao.otavio.authentication_domain.datasource

import com.joao.otavio.authentication_data.database.OrganizationDao
import com.joao.otavio.authentication_data.mapper.OrganizationMapper.toEntity
import com.joao.otavio.authentication_data.model.domain.Organization
import com.joao.otavio.authentication_presentation.datasource.AuthenticationLocalDataSource
import com.joao.otavio.core.datastore.DataStoreKeyConstants.WebTrackerAuthentication
import com.joao.otavio.core.datastore.WebTrackerDataStore
import com.joao.otavio.core.logger.WebTrackerLogger
import javax.inject.Inject

class AuthenticationLocalDataSourceImpl @Inject constructor(
    private val webTrackerDataStore: WebTrackerDataStore,
    private val organizationDao: OrganizationDao,
    private val logger: WebTrackerLogger
) : AuthenticationLocalDataSource {
    override suspend fun saveOrganizationInDatabase(organization: Organization): Boolean {
        return try {
            organizationDao.upsert(organization.toEntity())
        } catch (t: Throwable) {
            logger.e(logger.getTag(), "Unexpected error during saving organization in database", t)
            false
        }
    }

    override suspend fun saveOrganizationInDataStore(organizationId: Organization): Boolean {
        return try {
            webTrackerDataStore.savePreference(
                WebTrackerAuthentication.FIREBASE_ORG_ID,
                organizationId.organizationId
            )
            webTrackerDataStore.savePreference(
                WebTrackerAuthentication.FIREBASE_ORG_NAME,
                organizationId.organizationName
            )
            logger.i(logger.getTag(), "Successful save organization in Datastore")
            true
        } catch (t: Throwable) {
            logger.e(logger.getTag(), "Unexpected error during saving organization in datastore", t)
            false
        }
    }

    override suspend fun getOrganizationIdInDataStore(): String? {
        return try {
            logger.i(logger.getTag(), "Successful get organization in Datastore")
            webTrackerDataStore.getPreference(WebTrackerAuthentication.FIREBASE_ORG_ID)
        } catch (t: Throwable) {
            logger.e(logger.getTag(), "Unexpected error during catching organization in datastore", t)
            null
        }
    }
}
