package com.joao.otavio.authentication_domain.usecases

import com.joao.otavio.authentication_presentation.repository.AuthenticationRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CheckOrganizationLoginStatusUseCaseImplTest {

    private lateinit var useCase: CheckOrganizationLoginStatusUseCaseImpl
    private val authenticationRepository: AuthenticationRepository = mockk()

    @Before
    fun setup() {
        useCase = CheckOrganizationLoginStatusUseCaseImpl(
            authenticationRepository = authenticationRepository,
        )
    }

    @Test
    fun `when user is logged in then return Result success with true`() = runTest {
        // Mockk
        coEvery { authenticationRepository.isUserLoggedIn() } returns true

        // Run Test
        val result = useCase()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrNull())
    }

    @Test
    fun `when user is not logged in then return Result success with false`() = runTest {
        // Mockk
        coEvery { authenticationRepository.isUserLoggedIn() } returns false

        // Run Test
        val result = useCase()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(false, result.getOrNull())
    }

    @Test
    fun `when repository throws exception then return Result failure`() = runTest {
        // Mockk
        val exception = Exception("Failed to check login status")
        coEvery { authenticationRepository.isUserLoggedIn() } throws exception

        // Run Test
        val result = useCase()

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
