import com.joao.otavio.authentication_presentation.events.AuthenticationEvents
import com.joao.otavio.authentication_presentation.repository.AuthenticationRepository
import com.joao.otavio.authentication_presentation.state.AuthenticateState
import com.joao.otavio.authentication_presentation.utils.MainDispatcherRule
import com.joao.otavio.authentication_presentation.viewmodel.WebTrackerLoginViewModel
import com.joao.otavio.core.coroutine.CoroutineContextProvider
import com.joao.otavio.core.coroutine.TestContextProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WebTrackerLoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var testContextProvider: CoroutineContextProvider
    private val authenticationRepository: AuthenticationRepository = mockk()
    private lateinit var viewModel: WebTrackerLoginViewModel

    @Before
    fun setup() {
        testContextProvider = TestContextProvider(mainDispatcherRule.testDispatcher)
        coEvery { authenticationRepository.isUserLoggedIn() } returns false

        viewModel = WebTrackerLoginViewModel(
            authenticationRepository = authenticationRepository,
            coroutineContextProvider = testContextProvider
        )
    }

    @Test
    fun `given a user already loggedIn, when user enter in the app, then isAuthenticateSucceed should be true`() = runTest {
        // Mockk
        coEvery { authenticationRepository.isUserLoggedIn() } returns true

        // Run Test
        viewModel.isUserAlreadyLoggedIn()
        advanceUntilIdle()

        // Assert
        assertEquals(viewModel.webTrackerAuthenticationState.isAuthenticateSucceed.value, AuthenticateState.AUTHENTICATE)
        assertFalse(viewModel.webTrackerAuthenticationState.isLoading.value)
        coEvery {
            authenticationRepository.isUserLoggedIn()
        }
    }

    @Test
    fun `given a user not loggedIn, when user enter in the app, then isAuthenticateSucceed should be false`() = runTest {
        // Mockk
        coEvery { authenticationRepository.isUserLoggedIn() } returns false

        // Run Test
        viewModel.isUserAlreadyLoggedIn()
        advanceUntilIdle()

        // Assert
        assertEquals(viewModel.webTrackerAuthenticationState.isAuthenticateSucceed.value, AuthenticateState.IDLE)
        assertFalse(viewModel.webTrackerAuthenticationState.isLoading.value)
        coEvery {
            authenticationRepository.isUserLoggedIn()
        }
    }

    @Test
    fun `given a user types its email and password, when it tries to login and succeed, then isAuthenticateSucceed should be true`() = runTest {
        // Mockk
        coEvery {
            authenticationRepository.authenticateUserWithEmailAndPassword(
                USER_EMAIL,
                USER_PASSWORD
            )
        } returns true

        // Run Test
        viewModel.onUiEvents(AuthenticationEvents.OnTypingEmail(USER_EMAIL))
        viewModel.onUiEvents(AuthenticationEvents.OnTypingPassword(USER_PASSWORD))
        viewModel.onUiEvents(AuthenticationEvents.OnLoginUpClick)
        advanceUntilIdle()

        // Assert
        assertEquals(viewModel.webTrackerAuthenticationState.isAuthenticateSucceed.value, AuthenticateState.AUTHENTICATE)
        assertFalse(viewModel.webTrackerAuthenticationState.isLoading.value)
        assertFalse(viewModel.webTrackerAuthenticationState.displayErrorSnackBar.value)
        coVerify {
            authenticationRepository.authenticateUserWithEmailAndPassword(
                USER_EMAIL,
                USER_PASSWORD
            )
        }
    }

    @Test
    fun `given a user types its email and password, when it tries to login and fails, then isAuthenticateSucceed should be false`() = runTest {
        // Mockk
        coEvery {
            authenticationRepository.authenticateUserWithEmailAndPassword(
                USER_EMAIL,
                USER_PASSWORD
            )
        } returns false

        // Run Test
        viewModel.onUiEvents(AuthenticationEvents.OnTypingEmail(USER_EMAIL))
        viewModel.onUiEvents(AuthenticationEvents.OnTypingPassword(USER_PASSWORD))
        viewModel.onUiEvents(AuthenticationEvents.OnLoginUpClick)
        advanceUntilIdle()

        // Assert
        assertEquals(viewModel.webTrackerAuthenticationState.isAuthenticateSucceed.value, AuthenticateState.ERROR)
        assertFalse(viewModel.webTrackerAuthenticationState.isLoading.value)
        assertTrue(viewModel.webTrackerAuthenticationState.displayErrorSnackBar.value)
        coVerify {
            authenticationRepository.authenticateUserWithEmailAndPassword(
                USER_EMAIL,
                USER_PASSWORD
            )
        }
    }

    @Test
    fun `given a user press a button to signUp, then showLoginFields should be true`() = runTest {
        // Run Test
        viewModel.onUiEvents(AuthenticationEvents.OnDisplayLoginFieldsClick)

        // Assert
        assertTrue(viewModel.webTrackerAuthenticationState.showLoginFields.value)
    }

    @Test
    fun `given a user press a button to cancel, then showLoginFields should be false`() = runTest {
        // Run Test
        viewModel.onUiEvents(AuthenticationEvents.OnDisplayLoginFieldsClick)
        viewModel.onUiEvents(AuthenticationEvents.OnDisplayLoginFieldsClick)

        // Assert
        assertFalse(viewModel.webTrackerAuthenticationState.showLoginFields.value)
        assertEquals(viewModel.webTrackerAuthenticationState.userEmail.value, EMPTY_STRING)
        assertEquals(viewModel.webTrackerAuthenticationState.userPassword.value, EMPTY_STRING)
    }

    @Test
    fun `given a snackBar got display, after a time it should change its state, then it should be false`() = runTest {
        // Run Test
        viewModel.onUiEvents(AuthenticationEvents.OnSnackBarDismiss)

        // Assert
        assertFalse(viewModel.webTrackerAuthenticationState.displayErrorSnackBar.value)
    }

    companion object {
        const val USER_EMAIL = "user_email"
        const val USER_PASSWORD = "user_password"
        const val EMPTY_STRING = ""
    }
}