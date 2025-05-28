package com.joao.otavio.authentication_domain.datasource

import android.util.Log
import com.joao.otavio.core.datastore.DataStoreKeyConstants.WebTrackerAuthentication
import com.joao.otavio.core.datastore.WebTrackerDataStore
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class AuthenticationLocalDataSourceImplTest {
    private val webTrackerDataStore: WebTrackerDataStore = mockk()
    private val authenticationLocalDataSourceImpl = AuthenticationLocalDataSourceImpl(
        webTrackerDataStore = webTrackerDataStore
    )

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
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
            Log.i(any(), any())
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
            Log.e(any(), any(), any())
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
            Log.i(any(), any())
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
            Log.i(any(), any())
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
            Log.e(any(), any(), any())
        }
    }

    companion object {
        const val USER_ID = "user_id"
    }
}
