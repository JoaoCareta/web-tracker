package com.joao.otavio.authentication_domain.datasource

import com.joao.otavio.authentication_data.model.domain.Organization
import com.joao.otavio.authentication_presentation.authentication.Authentication
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AuthenticationRemoteDataSourceImplTest {
    private val authentication: Authentication = mockk()
    private val authenticationRemoteDataSourceImpl = AuthenticationRemoteDataSourceImpl(authentication)

    @Test
    fun `given a userEmail and userPassword, when firebase is able to authenticate, then it should return true`() = runTest {
        // Mockk
        coEvery { authentication.loginOrganizationWithEmailAndPassword(EMAIL, PASSWORD) } returns true

        // Run Test
        val result = authenticationRemoteDataSourceImpl.authenticateOrganization(EMAIL, PASSWORD)

        // Assert
        assertTrue(result)
        coVerify {
            authentication.loginOrganizationWithEmailAndPassword(EMAIL, PASSWORD)
        }
    }

    @Test
    fun `given a userEmail and userPassword, when firebase is not able to authenticate, then it should return false`() = runTest {
        // Mockk
        coEvery { authentication.loginOrganizationWithEmailAndPassword(EMAIL, PASSWORD) } returns false

        // Run Test
        val result = authenticationRemoteDataSourceImpl.authenticateOrganization(EMAIL, PASSWORD)

        // Assert
        assertFalse(result)
        coVerify {
            authentication.loginOrganizationWithEmailAndPassword(EMAIL, PASSWORD)
        }
    }

    @Test
    fun `given a loggedUser, when we try to get the userId and the authentication runs fine, then it should returns it`() = runTest {
        // Mockk
        coEvery { authentication.getLoginOrganization() } returns ORGANIZATION

        // Run Test
        val result = authenticationRemoteDataSourceImpl.getLoginOrganization()

        // Assert
        assertEquals(result, ORGANIZATION)
        coVerify {
            authentication.getLoginOrganization()
        }
    }

    @Test
    fun `given a loggedUser, when we try to get the userId and the authentication fails, then it should returns null`() = runTest {
        // Mockk
        coEvery { authentication.getLoginOrganization() } returns null

        // Run Test
        val result = authenticationRemoteDataSourceImpl.getLoginOrganization()

        // Assert
        assertNull(result)
        coVerify {
            authentication.getLoginOrganization()
        }
    }

    companion object {
        private const val ORGANIZATION_ID = "organization_id"
        private const val ORGANIZATION_NAME = "organization_name"
        const val EMAIL = "test@test.com"
        const val PASSWORD = "123456"
        val ORGANIZATION = Organization(
            organizationId = ORGANIZATION_ID,
            organizationName = ORGANIZATION_NAME
        )
    }
}
