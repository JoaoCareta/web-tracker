package com.joao.otavio.authentication_domain.datasource

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
    private val authenticationLocalDataSourceImpl = AuthenticationLocalDataSourceImpl(
        webTrackerDataStore = webTrackerDataStore,
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
        coJustRun { webTrackerDataStore.savePreference(WebTrackerAuthentication.FIREBASE_USER_ID, USER_ID) }

        // Run Test
        val result = authenticationLocalDataSourceImpl.saveUserIdInDataStore(USER_ID)

        // Assert
        assertTrue(result)
        coVerify {
            webTrackerDataStore.savePreference(WebTrackerAuthentication.FIREBASE_USER_ID, USER_ID)
            logger.i(any(), any())
        }
    }

    @Test
    fun `given a userId, when dataStore is not able to save it, then it should return false`() = runTest {
        // Mockk
        coEvery { webTrackerDataStore.savePreference(WebTrackerAuthentication.FIREBASE_USER_ID, USER_ID) } throws Exception()

        // Run Test
        val result = authenticationLocalDataSourceImpl.saveUserIdInDataStore(USER_ID)

        // Assert
        assertFalse(result)
        coVerify {
            webTrackerDataStore.savePreference(WebTrackerAuthentication.FIREBASE_USER_ID, USER_ID)
            logger.e(any(), any(), any())
        }
    }

    @Test
    fun `given a savedUser, when dataStore succeed in get it, then it should return it`() = runTest {
        // Mockk
        coEvery { webTrackerDataStore.getPreference(WebTrackerAuthentication.FIREBASE_USER_ID) } returns USER_ID

        // Run Test
        val result = authenticationLocalDataSourceImpl.getUserIdInDataStore()

        // Assert
        assertEquals(result, USER_ID)
        coVerify {
            webTrackerDataStore.getPreference(WebTrackerAuthentication.FIREBASE_USER_ID)
            logger.i(any(), any())
        }
    }

    @Test
    fun `given a savedUser, when dataStore returns null, then it should return null`() = runTest {
        // Mockk
        coEvery { webTrackerDataStore.getPreference(WebTrackerAuthentication.FIREBASE_USER_ID) } returns null

        // Run Test
        val result = authenticationLocalDataSourceImpl.getUserIdInDataStore()

        // Assert
        assertNull(result)
        coVerify {
            webTrackerDataStore.getPreference(WebTrackerAuthentication.FIREBASE_USER_ID)
            logger.i(any(), any())
        }
    }

    @Test
    fun `given a savedUser, when dataStore is not able to get it, then it should return null`() = runTest {
        // Mockk
        coEvery { webTrackerDataStore.getPreference(WebTrackerAuthentication.FIREBASE_USER_ID) } throws Exception()

        // Run Test
        val result = authenticationLocalDataSourceImpl.getUserIdInDataStore()

        // Assert
        assertNull(result)
        coVerify {
            webTrackerDataStore.getPreference(WebTrackerAuthentication.FIREBASE_USER_ID)
            logger.e(any(), any(), any())
        }
    }

    companion object {
        const val USER_ID = "user_id"
        const val CLASS_NAME = "AuthenticationLocalDataSourceImplTest"
    }
}
