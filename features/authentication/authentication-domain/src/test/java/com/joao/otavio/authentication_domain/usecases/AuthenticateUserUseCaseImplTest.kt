package com.joao.otavio.authentication_domain.usecases

import com.joao.otavio.authentication_presentation.repository.AuthenticationRepository
import com.joao.otavio.authentication_presentation.utils.MainDispatcherRule
import com.joao.otavio.core.coroutine.CoroutineContextProvider
import com.joao.otavio.core.coroutine.TestContextProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthenticateUserUseCaseImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var useCase: AuthenticateUserUseCaseImpl
    private val authenticationRepository: AuthenticationRepository = mockk()
    private lateinit var testContextProvider: CoroutineContextProvider

    @Before
    fun setup() {
        testContextProvider = TestContextProvider(mainDispatcherRule.testDispatcher)
        useCase = AuthenticateUserUseCaseImpl(
            authenticationRepository = authenticationRepository,
            coroutineContextProvider = testContextProvider
        )
    }

    @Test
    fun `when authentication is successful then return Result success`() = runBlockingTest {
        // Mockk
        val email = "test@email.com"
        val password = "password123"
        coEvery {
            authenticationRepository.authenticateUserWithEmailAndPassword(email, password)
        } returns true

        // Run Test
        val result = useCase(email, password)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrNull())
    }

    @Test
    fun `when authentication is fail then return Result error`() = runBlockingTest {
        // Mockk
        val email = "test@email.com"
        val password = "password123"
        coEvery {
            authenticationRepository.authenticateUserWithEmailAndPassword(email, password)
        } returns false

        // Run Test
        val result = useCase(email, password)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(false, result.getOrNull())
    }

    @Test
    fun `when authentication fails then return Result failure`() = runBlockingTest {
        // Mockk
        val email = "test@email.com"
        val password = "password123"
        val exception = Exception("Authentication failed")
        coEvery {
            authenticationRepository.authenticateUserWithEmailAndPassword(email, password)
        } throws exception

        // Run Test
        val result = useCase(email, password)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `when email is empty then return Result failure`() = runBlockingTest {
        // Mockk
        val email = ""
        val password = "password123"
        val exception = Exception("Invalid email")
        coEvery {
            authenticationRepository.authenticateUserWithEmailAndPassword(email, password)
        } throws exception

        // Run Test
        val result = useCase(email, password)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `when password is empty then return Result failure`() = runBlockingTest {
        // Mockk
        val email = "test@email.com"
        val password = ""
        val exception = Exception("Invalid password")
        coEvery {
            authenticationRepository.authenticateUserWithEmailAndPassword(email, password)
        } throws exception

        // Run Test
        val result = useCase(email, password)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
