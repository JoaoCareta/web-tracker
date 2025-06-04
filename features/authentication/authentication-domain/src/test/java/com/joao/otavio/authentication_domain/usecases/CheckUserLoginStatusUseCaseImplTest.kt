package com.joao.otavio.authentication_domain.usecases

import com.joao.otavio.authentication_presentation.repository.AuthenticationRepository
import com.joao.otavio.authentication_presentation.utils.MainDispatcherRule
import com.joao.otavio.core.coroutine.CoroutineContextProvider
import com.joao.otavio.core.coroutine.TestContextProvider
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CheckUserLoginStatusUseCaseImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var useCase: CheckUserLoginStatusUseCaseImpl
    private val authenticationRepository: AuthenticationRepository = mockk()
    private lateinit var testContextProvider: CoroutineContextProvider

    @Before
    fun setup() {
        testContextProvider = TestContextProvider(mainDispatcherRule.testDispatcher)
        useCase = CheckUserLoginStatusUseCaseImpl(
            authenticationRepository = authenticationRepository,
            coroutineContextProvider = testContextProvider
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
