import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.joao.otavio.authentication_presentation.events.AuthenticationEvents
import com.joao.otavio.authentication_presentation.state.AuthenticateState
import com.joao.otavio.authentication_presentation.state.AuthenticationErrorType
import com.joao.otavio.authentication_presentation.usecases.AuthenticateUserUseCase
import com.joao.otavio.authentication_presentation.usecases.CheckUserLoginStatusUseCase
import com.joao.otavio.authentication_presentation.utils.MainDispatcherRule
import com.joao.otavio.authentication_presentation.viewmodel.WebTrackerAuthenticationViewModel
import com.joao.otavio.core.coroutine.CoroutineContextProvider
import com.joao.otavio.core.coroutine.TestContextProvider
import io.mockk.coEvery
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
class WebTrackerAuthenticationViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var testContextProvider: CoroutineContextProvider
    private val authenticationUseCase: AuthenticateUserUseCase = mockk()
    private val checkUserLoginStatusUseCase: CheckUserLoginStatusUseCase = mockk()
    private val connectivityManager: ConnectivityManager = mockk()
    private val networkCapabilities: NetworkCapabilities = mockk()
    private lateinit var viewModel: WebTrackerAuthenticationViewModel

    @Before
    fun setup() {
        testContextProvider = TestContextProvider(mainDispatcherRule.testDispatcher)
        coEvery { checkUserLoginStatusUseCase() } returns Result.success(false)
        coEvery { connectivityManager.activeNetwork } returns mockk()
        coEvery { connectivityManager.getNetworkCapabilities(any()) } returns networkCapabilities
        coEvery {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } returns true
        coEvery {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } returns true

        viewModel = WebTrackerAuthenticationViewModel(
            authenticateUserUseCase = authenticationUseCase,
            checkUserLoginStatusUseCase = checkUserLoginStatusUseCase,
            connectivityManager = connectivityManager,
            coroutineContextProvider = testContextProvider
        )
    }

    /*
     * Login Status Check Tests
     */

    @Test
    fun `given user is logged in, when checking login status, then authentication state should be AUTHENTICATE`() = runTest {
        // Mockk
        coEvery { checkUserLoginStatusUseCase() } returns Result.success(true)

        // Run Test
        viewModel.isUserAlreadyLoggedIn()
        advanceUntilIdle()

        // Assert
        assertEquals(AuthenticateState.AUTHENTICATE, viewModel.webTrackerAuthenticationState.isAuthenticateSucceed.value)
        assertFalse(viewModel.webTrackerAuthenticationState.isLoading.value)
    }

    @Test
    fun `given login status check fails, when checking login status, then should show authentication failed error`() = runTest {
        // Mockk
        coEvery { checkUserLoginStatusUseCase() } returns Result.failure(Exception())

        // Run Test
        viewModel.isUserAlreadyLoggedIn()
        advanceUntilIdle()

        // Assert
        assertEquals(AuthenticationErrorType.AUTHENTICATION_FAILED, viewModel.webTrackerAuthenticationState.authenticationErrorType.value)
        assertTrue(viewModel.webTrackerAuthenticationState.displayErrorSnackBar.value)
    }

    @Test
    fun `given network is available, when checking login status and user is not logged in, then state should be IDLE`() = runTest {
        // Mockk
        coEvery { connectivityManager.getNetworkCapabilities(any()) } returns networkCapabilities
        coEvery { networkCapabilities.hasTransport(any()) } returns true
        coEvery { checkUserLoginStatusUseCase() } returns Result.success(false)

        // Run Test
        viewModel.isUserAlreadyLoggedIn()
        advanceUntilIdle()

        // Assert
        assertEquals(AuthenticateState.IDLE, viewModel.webTrackerAuthenticationState.isAuthenticateSucceed.value)
        assertFalse(viewModel.webTrackerAuthenticationState.isLoading.value)
    }

    @Test
    fun `given network is available, when checking login status, then loading should be shown and hidden`() = runTest {
        // Mockk
        coEvery { connectivityManager.getNetworkCapabilities(any()) } returns networkCapabilities
        coEvery { networkCapabilities.hasTransport(any()) } returns true
        coEvery { checkUserLoginStatusUseCase() } returns Result.success(true)

        // Run Test
        viewModel.isUserAlreadyLoggedIn()

        // Assert loading is true initially
        assertTrue(viewModel.webTrackerAuthenticationState.isLoading.value)

        // Wait for completion
        advanceUntilIdle()

        // Assert loading is false after completion
        assertFalse(viewModel.webTrackerAuthenticationState.isLoading.value)
    }

    @Test
    fun `given no network available, when checking login status, then should show error without calling use case`() = runTest {
        // Mockk
        coEvery { connectivityManager.getNetworkCapabilities(any()) } returns null

        // Run Test
        viewModel.isUserAlreadyLoggedIn()

        // Assert
        assertEquals(AuthenticationErrorType.NO_INTERNET_CONNECTION, viewModel.webTrackerAuthenticationState.authenticationErrorType.value)
        assertTrue(viewModel.webTrackerAuthenticationState.displayErrorSnackBar.value)
    }

    /*
     * Network Connectivity Tests
     */

    @Test
    fun `given no internet connection, when performing any authentication action, then should show no internet error`() = runTest {
        // Mockk
        coEvery { connectivityManager.getNetworkCapabilities(any()) } returns null

        // Run Test
        viewModel.isUserAlreadyLoggedIn()
        advanceUntilIdle()

        // Assert
        assertEquals(AuthenticationErrorType.NO_INTERNET_CONNECTION, viewModel.webTrackerAuthenticationState.authenticationErrorType.value)
        assertTrue(viewModel.webTrackerAuthenticationState.displayErrorSnackBar.value)
    }

    /*
     * Input Validation Tests
     */

    @Test
    fun `given empty email, when attempting to login, then should show empty email error`() = runTest {
        // Mockk
        // No mocks needed for this test

        // Run Test
        viewModel.onUiEvents(AuthenticationEvents.OnTypingEmail(""))
        viewModel.onUiEvents(AuthenticationEvents.OnLoginUpClick)

        // Assert
        assertEquals(AuthenticationErrorType.EMPTY_EMAIL, viewModel.webTrackerAuthenticationState.authenticationErrorType.value)
        assertTrue(viewModel.webTrackerAuthenticationState.displayErrorSnackBar.value)
    }

    @Test
    fun `given invalid email format, when attempting to login, then should show invalid email format error`() = runTest {
        // Mockk
        // No mocks needed for this test

        // Run Test
        viewModel.onUiEvents(AuthenticationEvents.OnTypingEmail("invalid-email"))
        viewModel.onUiEvents(AuthenticationEvents.OnLoginUpClick)

        // Assert
        assertEquals(AuthenticationErrorType.EMAIL_INVALID_FORMAT, viewModel.webTrackerAuthenticationState.authenticationErrorType.value)
        assertTrue(viewModel.webTrackerAuthenticationState.displayErrorSnackBar.value)
    }

    @Test
    fun `given empty password with valid email, when attempting to login, then should show empty password error`() = runTest {
        // Mockk
        // No mocks needed for this test

        // Run Test
        viewModel.onUiEvents(AuthenticationEvents.OnTypingEmail("valid@email.com"))
        viewModel.onUiEvents(AuthenticationEvents.OnTypingPassword(""))
        viewModel.onUiEvents(AuthenticationEvents.OnLoginUpClick)

        // Assert
        assertEquals(AuthenticationErrorType.EMPTY_PASSWORD, viewModel.webTrackerAuthenticationState.authenticationErrorType.value)
        assertTrue(viewModel.webTrackerAuthenticationState.displayErrorSnackBar.value)
    }

    /*
     * Authentication Tests
     */

    @Test
    fun `given valid credentials, when attempting to login, then authentication should succeed`() = runTest {
        // Mockk
        coEvery {
            authenticationUseCase(USER_EMAIL, USER_PASSWORD)
        } returns Result.success(true)

        // Run Test
        viewModel.onUiEvents(AuthenticationEvents.OnTypingEmail(USER_EMAIL))
        viewModel.onUiEvents(AuthenticationEvents.OnTypingPassword(USER_PASSWORD))
        viewModel.onUiEvents(AuthenticationEvents.OnLoginUpClick)
        advanceUntilIdle()

        // Assert
        assertEquals(AuthenticateState.AUTHENTICATE, viewModel.webTrackerAuthenticationState.isAuthenticateSucceed.value)
        assertFalse(viewModel.webTrackerAuthenticationState.displayErrorSnackBar.value)
    }

    @Test
    fun `given invalid credentials, when attempting to login, then should show authentication failed error`() = runTest {
        // Mockk
        coEvery {
            authenticationUseCase(USER_EMAIL, USER_PASSWORD)
        } returns Result.failure(Exception())

        // Run Test
        viewModel.onUiEvents(AuthenticationEvents.OnTypingEmail(USER_EMAIL))
        viewModel.onUiEvents(AuthenticationEvents.OnTypingPassword(USER_PASSWORD))
        viewModel.onUiEvents(AuthenticationEvents.OnLoginUpClick)
        advanceUntilIdle()

        // Assert
        assertEquals(AuthenticationErrorType.AUTHENTICATION_FAILED, viewModel.webTrackerAuthenticationState.authenticationErrorType.value)
        assertTrue(viewModel.webTrackerAuthenticationState.displayErrorSnackBar.value)
    }

    /*
     * UI State Tests
     */

    @Test
    fun `given login fields are hidden, when display login fields clicked, then should show fields and clear inputs`() = runTest {
        // Mockk
        // No mocks needed for this test

        // Run Test
        viewModel.onUiEvents(AuthenticationEvents.OnDisplayLoginFieldsClick)

        // Assert
        assertTrue(viewModel.webTrackerAuthenticationState.showLoginFields.value)
        assertEquals("", viewModel.webTrackerAuthenticationState.userEmail.value)
        assertEquals("", viewModel.webTrackerAuthenticationState.userPassword.value)
    }

    @Test
    fun `given error snackbar is shown, when dismissed, then should hide snackbar`() = runTest {
        // Mockk
        // No mocks needed for this test

        // Run Test
        viewModel.onUiEvents(AuthenticationEvents.OnSnackBarDismiss)

        // Assert
        assertFalse(viewModel.webTrackerAuthenticationState.displayErrorSnackBar.value)
    }

    /*
 * Email Input Tests
 */

    @Test
    fun `given user types email, when email is updated, then state should reflect new email`() = runTest {
        // Run Test
        val newEmail = "new@email.com"
        viewModel.onUiEvents(AuthenticationEvents.OnTypingEmail(newEmail))

        // Assert
        assertEquals(newEmail, viewModel.webTrackerAuthenticationState.userEmail.value)
    }

    @Test
    fun `given user types email with spaces, when attempting login, then email should be trimmed`() = runTest {
        // Mockk
        coEvery {
            authenticationUseCase("test@email.com", USER_PASSWORD)
        } returns Result.success(true)

        // Run Test
        viewModel.onUiEvents(AuthenticationEvents.OnTypingEmail("  test@email.com  "))
        viewModel.onUiEvents(AuthenticationEvents.OnTypingPassword(USER_PASSWORD))
        viewModel.onUiEvents(AuthenticationEvents.OnLoginUpClick)
        advanceUntilIdle()

        // Assert
        assertEquals(AuthenticateState.AUTHENTICATE, viewModel.webTrackerAuthenticationState.isAuthenticateSucceed.value)
    }

    /*
     * Password Input Tests
     */

    @Test
    fun `given user types password, when password is updated, then state should reflect new password`() = runTest {
        // Run Test
        val newPassword = "newPassword123"
        viewModel.onUiEvents(AuthenticationEvents.OnTypingPassword(newPassword))

        // Assert
        assertEquals(newPassword, viewModel.webTrackerAuthenticationState.userPassword.value)
    }

    /*
     * Network State Tests
     */

    @Test
    fun `given only wifi available, when checking network, then should consider network available`() = runTest {
        // Mockk
        coEvery { connectivityManager.getNetworkCapabilities(any()) } returns networkCapabilities
        coEvery { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
        coEvery { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false

        // Run Test
        viewModel.isUserAlreadyLoggedIn()
        advanceUntilIdle()

        // Assert
        assertFalse(viewModel.webTrackerAuthenticationState.displayErrorSnackBar.value)
    }

    @Test
    fun `given only cellular available, when checking network, then should consider network available`() = runTest {
        // Mockk
        coEvery { connectivityManager.getNetworkCapabilities(any()) } returns networkCapabilities
        coEvery { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        coEvery { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true

        // Run Test
        viewModel.isUserAlreadyLoggedIn()
        advanceUntilIdle()

        // Assert
        assertFalse(viewModel.webTrackerAuthenticationState.displayErrorSnackBar.value)
    }

    /*
     * Login Fields Visibility Tests
     */

    @Test
    fun `given login fields are shown, when display login fields clicked, then should hide fields`() = runTest {
        // Setup
        viewModel.onUiEvents(AuthenticationEvents.OnDisplayLoginFieldsClick) // Show fields first

        // Run Test
        viewModel.onUiEvents(AuthenticationEvents.OnDisplayLoginFieldsClick)

        // Assert
        assertFalse(viewModel.webTrackerAuthenticationState.showLoginFields.value)
    }

    @Test
    fun `given fields contain data, when toggling login fields visibility, then should clear all fields`() = runTest {
        // Setup
        viewModel.onUiEvents(AuthenticationEvents.OnTypingEmail(USER_EMAIL))
        viewModel.onUiEvents(AuthenticationEvents.OnTypingPassword(USER_PASSWORD))

        // Run Test
        viewModel.onUiEvents(AuthenticationEvents.OnDisplayLoginFieldsClick)

        // Assert
        assertEquals("", viewModel.webTrackerAuthenticationState.userEmail.value)
        assertEquals("", viewModel.webTrackerAuthenticationState.userPassword.value)
    }

    /*
     * Authentication Process Tests
     */

    @Test
    fun `given authentication in progress, when attempting login, then loading state should be updated correctly`() = runTest {
        // Mockk
        coEvery {
            authenticationUseCase(USER_EMAIL, USER_PASSWORD)
        } returns Result.success(true)

        // Run Test
        viewModel.onUiEvents(AuthenticationEvents.OnTypingEmail(USER_EMAIL))
        viewModel.onUiEvents(AuthenticationEvents.OnTypingPassword(USER_PASSWORD))
        viewModel.onUiEvents(AuthenticationEvents.OnLoginUpClick)

        // Wait for completion
        advanceUntilIdle()

        // Assert loading is false after completion
        assertFalse(viewModel.webTrackerAuthenticationState.isLoading.value)
    }

    @Test
    fun `given authentication succeeds but returns false, when attempting login, then should show authentication failed`() = runTest {
        // Mockk
        coEvery {
            authenticationUseCase(USER_EMAIL, USER_PASSWORD)
        } returns Result.success(false)

        // Run Test
        viewModel.onUiEvents(AuthenticationEvents.OnTypingEmail(USER_EMAIL))
        viewModel.onUiEvents(AuthenticationEvents.OnTypingPassword(USER_PASSWORD))
        viewModel.onUiEvents(AuthenticationEvents.OnLoginUpClick)
        advanceUntilIdle()

        // Assert
        assertEquals(AuthenticateState.ERROR, viewModel.webTrackerAuthenticationState.isAuthenticateSucceed.value)
        assertEquals(AuthenticationErrorType.AUTHENTICATION_FAILED, viewModel.webTrackerAuthenticationState.authenticationErrorType.value)
    }

    /*
     * Error Handling Tests
     */

    @Test
    fun `given multiple errors occur, when error is shown and dismissed, then should handle multiple error states correctly`() = runTest {
        // Run Test - First error
        viewModel.onUiEvents(AuthenticationEvents.OnLoginUpClick) // Will trigger EMPTY_EMAIL error
        assertTrue(viewModel.webTrackerAuthenticationState.displayErrorSnackBar.value)
        assertEquals(AuthenticationErrorType.EMPTY_EMAIL, viewModel.webTrackerAuthenticationState.authenticationErrorType.value)

        // Dismiss error
        viewModel.onUiEvents(AuthenticationEvents.OnSnackBarDismiss)
        assertFalse(viewModel.webTrackerAuthenticationState.displayErrorSnackBar.value)

        // Run Test - Second error
        viewModel.onUiEvents(AuthenticationEvents.OnTypingEmail("invalid-email"))
        viewModel.onUiEvents(AuthenticationEvents.OnLoginUpClick)
        assertTrue(viewModel.webTrackerAuthenticationState.displayErrorSnackBar.value)
        assertEquals(AuthenticationErrorType.EMAIL_INVALID_FORMAT, viewModel.webTrackerAuthenticationState.authenticationErrorType.value)
    }

    @Test
    fun `given network becomes unavailable during authentication, when attempting login, then should show network error`() = runTest {
        // Setup
        viewModel.onUiEvents(AuthenticationEvents.OnTypingEmail(USER_EMAIL))
        viewModel.onUiEvents(AuthenticationEvents.OnTypingPassword(USER_PASSWORD))

        // Mockk - Simulate network becoming unavailable
        coEvery { connectivityManager.getNetworkCapabilities(any()) } returns null

        // Run Test
        viewModel.onUiEvents(AuthenticationEvents.OnLoginUpClick)

        // Assert
        assertEquals(AuthenticationErrorType.NO_INTERNET_CONNECTION, viewModel.webTrackerAuthenticationState.authenticationErrorType.value)
        assertTrue(viewModel.webTrackerAuthenticationState.displayErrorSnackBar.value)
    }

    companion object {
        const val USER_EMAIL = "test@email.com"
        const val USER_PASSWORD = "password123"
    }
}
