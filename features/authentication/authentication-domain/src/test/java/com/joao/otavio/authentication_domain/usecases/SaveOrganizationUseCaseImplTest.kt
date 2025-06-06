package com.joao.otavio.authentication_domain.usecases

import com.joao.otavio.authentication_data.model.domain.Organization
import com.joao.otavio.authentication_presentation.datasource.AuthenticationLocalDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveOrganizationUseCaseImplTest {
    private val authenticationLocalDataSource: AuthenticationLocalDataSource = mockk()
    private lateinit var saveOrganizationUseCaseImpl: SaveOrganizationUseCaseImpl

    @Before
    fun setup() {
        saveOrganizationUseCaseImpl = SaveOrganizationUseCaseImpl(
            authenticationLocalDataSource = authenticationLocalDataSource
        )
    }

    @Test
    fun `given a organization, when localDataSource fails to save it into database, then it should return false`() = runTest {
        // Mockk
        coEvery { authenticationLocalDataSource.saveOrganizationInDatabase(ORGANIZATION) } returns false

        // Run Test
        val result = saveOrganizationUseCaseImpl.invoke(ORGANIZATION)

        // Assert
        assertFalse(result)
        coVerify {
            authenticationLocalDataSource.saveOrganizationInDatabase(ORGANIZATION)
        }
    }

    @Test
    fun `given a organization, when localDataSource fails to save it into datastore, then it should return false`() = runTest {
        // Mockk
        coEvery { authenticationLocalDataSource.saveOrganizationInDatabase(ORGANIZATION) } returns true
        coEvery { authenticationLocalDataSource.saveOrganizationInDataStore(ORGANIZATION) } returns false

        // Run Test
        val result = saveOrganizationUseCaseImpl.invoke(ORGANIZATION)

        // Assert
        assertFalse(result)
        coVerify {
            authenticationLocalDataSource.saveOrganizationInDatabase(ORGANIZATION)
            authenticationLocalDataSource.saveOrganizationInDataStore(ORGANIZATION)
        }
    }

    @Test
    fun `given a organization, when localDataSource succeed to save it, then it should return true`() = runTest {
        // Mockk
        coEvery { authenticationLocalDataSource.saveOrganizationInDatabase(ORGANIZATION) } returns true
        coEvery { authenticationLocalDataSource.saveOrganizationInDataStore(ORGANIZATION) } returns true

        // Run Test
        val result = saveOrganizationUseCaseImpl.invoke(ORGANIZATION)

        // Assert
        assertTrue(result)
        coVerify {
            authenticationLocalDataSource.saveOrganizationInDatabase(ORGANIZATION)
            authenticationLocalDataSource.saveOrganizationInDataStore(ORGANIZATION)
        }
    }

    companion object {
        private const val ORGANIZATION_ID = "organization_id"
        private const val ORGANIZATION_NAME = "organization_name"
        val ORGANIZATION = Organization(
            organizationId = ORGANIZATION_ID,
            organizationName = ORGANIZATION_NAME
        )
    }
}
