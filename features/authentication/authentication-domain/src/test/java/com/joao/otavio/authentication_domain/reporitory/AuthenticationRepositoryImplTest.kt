package com.joao.otavio.authentication_domain.reporitory

import android.util.Log
import com.joao.otavio.authentication_data.model.domain.Organization
import com.joao.otavio.authentication_domain.repository.AuthenticationRepositoryImpl
import com.joao.otavio.authentication_presentation.datasource.AuthenticationLocalDataSource
import com.joao.otavio.authentication_presentation.datasource.AuthenticationRemoteDataSource
import com.joao.otavio.authentication_presentation.usecases.SaveOrganizationUseCase
import com.joao.otavio.core.logger.WebTrackerLogger
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthenticationRepositoryImplTest {
    private val authenticationLocalDataSource: AuthenticationLocalDataSource = mockk()
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource = mockk()
    private val saveOrganizationUseCase: SaveOrganizationUseCase = mockk()
    private val logger: WebTrackerLogger = mockk()

    private val authenticationRepositoryImpl = AuthenticationRepositoryImpl(
        authenticationLocalDataSource = authenticationLocalDataSource,
        authenticationRemoteDataSource = authenticationRemoteDataSource,
        saveOrganizationUseCase = saveOrganizationUseCase,
        logger = logger
    )

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { logger.getTag() } returns CLASS_NAME
        justRun { logger.i(any(), any()) }
        justRun { logger.w(any(), any()) }
        justRun { logger.e(any(), any()) }
        justRun { logger.e(any(), any(), any()) }
    }

    @Test
    fun `given a userEmail and userPassword, when remoteDataSource fails to authenticate, then it should return false`() = runTest {
        // Mockk
        coEvery { authenticationRemoteDataSource.authenticateOrganization(USER_EMAIL, USER_PASSWORD) } returns false

        // Run Test
        val result = authenticationRepositoryImpl.authenticateUserWithEmailAndPassword(USER_EMAIL, USER_PASSWORD)

        // Assert
        assertFalse(result)
        coVerify {
            authenticationRemoteDataSource.authenticateOrganization(USER_EMAIL, USER_PASSWORD)
            logger.e(any(), any())
        }
    }

    @Test
    fun `given a userEmail and userPassword, when remoteDataSource fails to authenticate and throw and exception, then it should return false`() = runTest {
        // Mockk
        coEvery { authenticationRemoteDataSource.authenticateOrganization(USER_EMAIL, USER_PASSWORD) } throws Exception()

        // Run Test
        val result = authenticationRepositoryImpl.authenticateUserWithEmailAndPassword(USER_EMAIL, USER_PASSWORD)

        // Assert
        assertFalse(result)
        coVerify {
            authenticationRemoteDataSource.authenticateOrganization(USER_EMAIL, USER_PASSWORD)
            logger.e(any(), any(), any())
        }
    }

    @Test
    fun `given a userEmail and userPassword, when remoteDataSource succeed to authenticate but return a null userId, then it should return false`() = runTest {
        // Mockk
        coEvery { authenticationRemoteDataSource.authenticateOrganization(USER_EMAIL, USER_PASSWORD) } returns true
        coEvery { authenticationRemoteDataSource.getLoginOrganization() } returns null

        // Run Test
        val result = authenticationRepositoryImpl.authenticateUserWithEmailAndPassword(USER_EMAIL, USER_PASSWORD)

        // Assert
        assertFalse(result)
        coVerify {
            authenticationRemoteDataSource.authenticateOrganization(USER_EMAIL, USER_PASSWORD)
            authenticationRemoteDataSource.getLoginOrganization()
            logger.i(any(), any())
            logger.w(any(), any())
        }
    }

    @Test
    fun `given a userEmail and userPassword, when remoteDataSource succeed to authenticate and return a valid userId but the localDataSource fails to save it, then it should return false`() = runTest {
        // Mockk
        coEvery { authenticationRemoteDataSource.authenticateOrganization(USER_EMAIL, USER_PASSWORD) } returns true
        coEvery { authenticationRemoteDataSource.getLoginOrganization() } returns ORGANIZATION
        coEvery { saveOrganizationUseCase.invoke(ORGANIZATION) } returns false

        // Run Test
        val result = authenticationRepositoryImpl.authenticateUserWithEmailAndPassword(USER_EMAIL, USER_PASSWORD)

        // Assert
        assertFalse(result)
        coVerify {
            authenticationRemoteDataSource.authenticateOrganization(USER_EMAIL, USER_PASSWORD)
            authenticationRemoteDataSource.getLoginOrganization()
            saveOrganizationUseCase.invoke(ORGANIZATION)
            logger.i(any(), any())
        }
    }

    @Test
    fun `given a userEmail and userPassword, when remoteDataSource succeed to authenticate and return a valid userId and everything succeed, then it should return true`() = runTest {
        // Mockk
        coEvery { authenticationRemoteDataSource.authenticateOrganization(USER_EMAIL, USER_PASSWORD) } returns true
        coEvery { authenticationRemoteDataSource.getLoginOrganization() } returns ORGANIZATION
        coEvery { saveOrganizationUseCase.invoke(ORGANIZATION) } returns true

        // Run Test
        val result = authenticationRepositoryImpl.authenticateUserWithEmailAndPassword(USER_EMAIL, USER_PASSWORD)

        // Assert
        assertTrue(result)
        coVerify {
            authenticationRemoteDataSource.authenticateOrganization(USER_EMAIL, USER_PASSWORD)
            authenticationRemoteDataSource.getLoginOrganization()
            saveOrganizationUseCase.invoke(ORGANIZATION)
            logger.i(any(), any())
        }
    }

    @Test
    fun `given a not logged user, when it tries to get the userId, then it should return false`() = runTest {
        // Mockk
        coEvery { authenticationLocalDataSource.getOrganizationIdInDataStore() } returns null

        // Run Test
        val result = authenticationRepositoryImpl.isUserLoggedIn()

        // Assert
        assertFalse(result)
        coVerify {
            authenticationLocalDataSource.getOrganizationIdInDataStore()
            logger.i(any(), any())
        }
    }

    @Test
    fun `given a logged user, when it tries to get the userId and return a blank string, then it should return false`() = runTest {
        // Mockk
        coEvery { authenticationLocalDataSource.getOrganizationIdInDataStore() } returns BLANK_STRING

        // Run Test
        val result = authenticationRepositoryImpl.isUserLoggedIn()

        // Assert
        assertFalse(result)
        coVerify {
            authenticationLocalDataSource.getOrganizationIdInDataStore()
            logger.i(any(), any())
        }
    }

    @Test
    fun `given a logged user, when it tries to get the userId and return a empty string, then it should return false`() = runTest {
        // Mockk
        coEvery { authenticationLocalDataSource.getOrganizationIdInDataStore() } returns EMPTY_STRING

        // Run Test
        val result = authenticationRepositoryImpl.isUserLoggedIn()

        // Assert
        assertFalse(result)
        coVerify {
            authenticationLocalDataSource.getOrganizationIdInDataStore()
            logger.i(any(), any())
        }
    }

    @Test
    fun `given a logged user, when it tries to get the userId and return it, then it should return true`() = runTest {
        // Mockk
        coEvery { authenticationLocalDataSource.getOrganizationIdInDataStore() } returns ORGANIZATION_ID

        // Run Test
        val result = authenticationRepositoryImpl.isUserLoggedIn()

        // Assert
        assertTrue(result)
        coVerify {
            authenticationLocalDataSource.getOrganizationIdInDataStore()
            logger.i(any(), any())
        }
    }

    companion object {
        const val USER_EMAIL = "user_email"
        const val USER_PASSWORD = "user_password"
        const val ORGANIZATION_ID = "organization_id"
        const val ORGANIZATION_NAME = "organization_name"
        const val BLANK_STRING = " "
        const val EMPTY_STRING = ""
        const val CLASS_NAME = "AuthenticationRepositoryImpl"
        val ORGANIZATION = Organization(
            organizationUuid = ORGANIZATION_ID,
            organizationName = ORGANIZATION_NAME
        )
    }
}
