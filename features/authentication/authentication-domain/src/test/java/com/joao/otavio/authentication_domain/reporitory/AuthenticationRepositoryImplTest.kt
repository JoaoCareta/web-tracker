package com.joao.otavio.authentication_domain.reporitory

import android.util.Log
import com.joao.otavio.authentication_domain.repository.AuthenticationRepositoryImpl
import com.joao.otavio.authentication_presentation.datasource.AuthenticationLocalDataSource
import com.joao.otavio.authentication_presentation.datasource.AuthenticationRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class AuthenticationRepositoryImplTest {
    private val authenticationLocalDataSource: AuthenticationLocalDataSource = mockk()
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource = mockk()

    private val authenticationRepositoryImpl = AuthenticationRepositoryImpl(
        authenticationLocalDataSource = authenticationLocalDataSource,
        authenticationRemoteDataSource = authenticationRemoteDataSource
    )

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.w(any(), any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `given a userEmail and userPassword, when remoteDataSource fails to authenticate, then it should return false`() = runTest {
        // Mockk
        coEvery { authenticationRemoteDataSource.authenticateUser(USER_EMAIL, USER_PASSWORD) } returns false

        // Run Test
        val result = authenticationRepositoryImpl.authenticateUserWithEmailAndPassword(USER_EMAIL, USER_PASSWORD)

        // Assert
        assertFalse(result)
        coVerify {
            authenticationRemoteDataSource.authenticateUser(USER_EMAIL, USER_PASSWORD)
            Log.e(any(), any())
        }
    }

    @Test
    fun `given a userEmail and userPassword, when remoteDataSource succeed to authenticate but return a null userId, then it should return false`() = runTest {
        // Mockk
        coEvery { authenticationRemoteDataSource.authenticateUser(USER_EMAIL, USER_PASSWORD) } returns true
        coEvery { authenticationRemoteDataSource.getLoginUserId() } returns null

        // Run Test
        val result = authenticationRepositoryImpl.authenticateUserWithEmailAndPassword(USER_EMAIL, USER_PASSWORD)

        // Assert
        assertFalse(result)
        coVerify {
            authenticationRemoteDataSource.authenticateUser(USER_EMAIL, USER_PASSWORD)
            authenticationRemoteDataSource.getLoginUserId()
            Log.w(any<String>(), any<String>())
        }
    }

    @Test
    fun `given a userEmail and userPassword, when remoteDataSource succeed to authenticate and return a valid userId but the localDataSource fails to save it, then it should return false`() = runTest {
        // Mockk
        coEvery { authenticationRemoteDataSource.authenticateUser(USER_EMAIL, USER_PASSWORD) } returns true
        coEvery { authenticationRemoteDataSource.getLoginUserId() } returns USER_ID
        coEvery { authenticationLocalDataSource.saveUserIdInDataStore(USER_ID) } returns false

        // Run Test
        val result = authenticationRepositoryImpl.authenticateUserWithEmailAndPassword(USER_EMAIL, USER_PASSWORD)

        // Assert
        assertFalse(result)
        coVerify {
            authenticationRemoteDataSource.authenticateUser(USER_EMAIL, USER_PASSWORD)
            authenticationRemoteDataSource.getLoginUserId()
            authenticationLocalDataSource.saveUserIdInDataStore(USER_ID)
        }
    }

    @Test
    fun `given a userEmail and userPassword, when remoteDataSource succeed to authenticate and return a valid userId and everything succeed, then it should return true`() = runTest {
        // Mockk
        coEvery { authenticationRemoteDataSource.authenticateUser(USER_EMAIL, USER_PASSWORD) } returns true
        coEvery { authenticationRemoteDataSource.getLoginUserId() } returns USER_ID
        coEvery { authenticationLocalDataSource.saveUserIdInDataStore(USER_ID) } returns true

        // Run Test
        val result = authenticationRepositoryImpl.authenticateUserWithEmailAndPassword(USER_EMAIL, USER_PASSWORD)

        // Assert
        assertTrue(result)
        coVerify {
            authenticationRemoteDataSource.authenticateUser(USER_EMAIL, USER_PASSWORD)
            authenticationRemoteDataSource.getLoginUserId()
            authenticationLocalDataSource.saveUserIdInDataStore(USER_ID)
        }
    }

    @Test
    fun `given a not logged user, when it tries to get the userId, then it should return false`() = runTest {
        // Mockk
        coEvery { authenticationLocalDataSource.getUserIdInDataStore() } returns null

        // Run Test
        val result = authenticationRepositoryImpl.isUserLoggedIn()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `given a logged user, when it tries to get the userId and return a blank string, then it should return false`() = runTest {
        // Mockk
        coEvery { authenticationLocalDataSource.getUserIdInDataStore() } returns BLANK_STRING

        // Run Test
        val result = authenticationRepositoryImpl.isUserLoggedIn()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `given a logged user, when it tries to get the userId and return a empty string, then it should return false`() = runTest {
        // Mockk
        coEvery { authenticationLocalDataSource.getUserIdInDataStore() } returns EMPTY_STRING

        // Run Test
        val result = authenticationRepositoryImpl.isUserLoggedIn()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `given a logged user, when it tries to get the userId and return it, then it should return true`() = runTest {
        // Mockk
        coEvery { authenticationLocalDataSource.getUserIdInDataStore() } returns USER_ID

        // Run Test
        val result = authenticationRepositoryImpl.isUserLoggedIn()

        // Assert
        assertTrue(result)
    }

    companion object {
        const val USER_EMAIL = "user_email"
        const val USER_PASSWORD = "user_password"
        const val USER_ID = "user_id"
        const val BLANK_STRING = " "
        const val EMPTY_STRING = ""
    }
}
