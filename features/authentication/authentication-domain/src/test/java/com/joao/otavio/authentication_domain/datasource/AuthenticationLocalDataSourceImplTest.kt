package com.joao.otavio.authentication_domain.datasource

import com.joao.otavio.authentication_data.database.OrganizationDao
import com.joao.otavio.authentication_data.mapper.OrganizationMapper.toEntity
import com.joao.otavio.authentication_data.model.domain.Organization
import com.joao.otavio.core.datastore.DataStoreKeyConstants.WebTrackerAuthentication
import com.joao.otavio.core.datastore.WebTrackerDataStore
import com.joao.otavio.core.logger.WebTrackerLogger
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthenticationLocalDataSourceImplTest {
    private val webTrackerDataStore: WebTrackerDataStore = mockk()
    private val logger: WebTrackerLogger = mockk()
    private val organizationDao: OrganizationDao = mockk()
    private val authenticationLocalDataSourceImpl = AuthenticationLocalDataSourceImpl(
        webTrackerDataStore = webTrackerDataStore,
        organizationDao = organizationDao,
        logger = logger
    )

    @Before
    fun setUp() {
        every { logger.getTag() } returns CLASS_NAME
        justRun { logger.i(any(), any()) }
        justRun { logger.w(any(), any()) }
        justRun { logger.e(any(), any(), any()) }
    }

    @Test
    fun `given a userId, when dataStore is able to save it, then it should return true`() = runTest {
        // Mockk
        coJustRun { webTrackerDataStore.savePreference(WebTrackerAuthentication.FIREBASE_ORG_ID, ORGANIZATION_ID) }
        coJustRun { webTrackerDataStore.savePreference(WebTrackerAuthentication.FIREBASE_ORG_NAME, ORGANIZATION_NAME) }

        // Run Test
        val result = authenticationLocalDataSourceImpl.saveOrganizationInDataStore(ORGANIZATION)

        // Assert
        assertTrue(result)
        coVerify {
            webTrackerDataStore.savePreference(WebTrackerAuthentication.FIREBASE_ORG_ID, ORGANIZATION_ID)
            webTrackerDataStore.savePreference(WebTrackerAuthentication.FIREBASE_ORG_NAME, ORGANIZATION_NAME)
            logger.i(any(), any())
        }
    }

    @Test
    fun `given a userId, when dataStore is not able to save it, then it should return false`() = runTest {
        // Mockk
        coEvery { webTrackerDataStore.savePreference(WebTrackerAuthentication.FIREBASE_ORG_ID, ORGANIZATION_ID) } throws Exception()

        // Run Test
        val result = authenticationLocalDataSourceImpl.saveOrganizationInDataStore(ORGANIZATION)

        // Assert
        assertFalse(result)
        coVerify {
            webTrackerDataStore.savePreference(WebTrackerAuthentication.FIREBASE_ORG_ID, ORGANIZATION_ID)
            logger.e(any(), any(), any())
        }
    }

    @Test
    fun `given a savedUser, when dataStore succeed in get it, then it should return it`() = runTest {
        // Mockk
        coEvery { webTrackerDataStore.getPreference(WebTrackerAuthentication.FIREBASE_ORG_ID) } returns ORGANIZATION_ID

        // Run Test
        val result = authenticationLocalDataSourceImpl.getOrganizationIdInDataStore()

        // Assert
        assertEquals(result, ORGANIZATION_ID)
        coVerify {
            webTrackerDataStore.getPreference(WebTrackerAuthentication.FIREBASE_ORG_ID)
            logger.i(any(), any())
        }
    }

    @Test
    fun `given a savedUser, when dataStore returns null, then it should return null`() = runTest {
        // Mockk
        coEvery { webTrackerDataStore.getPreference(WebTrackerAuthentication.FIREBASE_ORG_ID) } returns null

        // Run Test
        val result = authenticationLocalDataSourceImpl.getOrganizationIdInDataStore()

        // Assert
        assertNull(result)
        coVerify {
            webTrackerDataStore.getPreference(WebTrackerAuthentication.FIREBASE_ORG_ID)
            logger.i(any(), any())
        }
    }

    @Test
    fun `given a savedUser, when dataStore is not able to get it, then it should return null`() = runTest {
        // Mockk
        coEvery { webTrackerDataStore.getPreference(WebTrackerAuthentication.FIREBASE_ORG_ID) } throws Exception()

        // Run Test
        val result = authenticationLocalDataSourceImpl.getOrganizationIdInDataStore()

        // Assert
        assertNull(result)
        coVerify {
            webTrackerDataStore.getPreference(WebTrackerAuthentication.FIREBASE_ORG_ID)
            logger.e(any(), any(), any())
        }
    }

    @Test
    fun `given an organization, when organizationDao is able to save it, then it should return true`() = runTest {
        // Mockk
        coEvery { organizationDao.upsert(ORGANIZATION.toEntity()) } returns true

        // Run Test
        val result = authenticationLocalDataSourceImpl.saveOrganizationInDatabase(ORGANIZATION)

        // Assert
        assertTrue(result)
        coVerify {
            organizationDao.upsert(ORGANIZATION.toEntity())
        }
    }

    @Test
    fun `given an organization, when organizationDao is not able to save it, then it should return false`() = runTest {
        // Mockk
        coEvery { organizationDao.upsert(ORGANIZATION.toEntity()) } returns false

        // Run Test
        val result = authenticationLocalDataSourceImpl.saveOrganizationInDatabase(ORGANIZATION)

        // Assert
        assertFalse(result)
        coVerify {
            organizationDao.upsert(ORGANIZATION.toEntity())
        }
    }

    @Test
    fun `given an organization, when organizationDao is not able to save it and throws and Exception, then it should return false`() = runTest {
        // Mockk
        coEvery { organizationDao.upsert(ORGANIZATION.toEntity()) } throws Exception()

        // Run Test
        val result = authenticationLocalDataSourceImpl.saveOrganizationInDatabase(ORGANIZATION)

        // Assert
        assertFalse(result)
    }

    companion object {
        const val ORGANIZATION_ID = "organization_id"
        const val ORGANIZATION_NAME = "organization_name"
        const val CLASS_NAME = "AuthenticationLocalDataSourceImplTest"
        val ORGANIZATION = Organization(
            organizationUuid = ORGANIZATION_ID,
            organizationName = ORGANIZATION_NAME
        )
    }
}
