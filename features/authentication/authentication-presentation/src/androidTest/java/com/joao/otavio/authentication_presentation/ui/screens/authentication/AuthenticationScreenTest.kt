package com.joao.otavio.authentication_presentation.ui.screens.authentication

import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.ImeAction
import com.joao.otavio.authentication_presentation.state.AuthenticateState
import com.joao.otavio.authentication_presentation.viewmodel.FakeAuthenticationViewModel
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.webtracker.common.desygn.system.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthenticationScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<androidx.activity.ComponentActivity>()

    private var navigateCalled = false
    private val expectedVersion = "1.0.0"
    private val fakeLoginViewModel = FakeAuthenticationViewModel()

    @Before
    fun setup() {
        MainScope().launch {
            composeTestRule.activity.setContent {
                WebTrackerTheme {
                    AuthenticationScreen(
                        version = expectedVersion,
                        onEnterClick = { navigateCalled = true },
                        authenticationViewModel = fakeLoginViewModel
                    )
                }
            }
        }
    }

    @Test
    fun initial_state_should_show_sign_in_button_and_hide_login_fields() = runTest {
        // Run Test - Initial state verification
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_signIn))
            .assertIsDisplayed()

        // Assert - Login fields should be hidden
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_email))
            .assertIsNotDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_password))
            .assertIsNotDisplayed()
    }

    @Test
    fun clicking_sign_in_button_should_display_login_fields_and_login_button() = runTest {
        // Run Test - Click sign in button
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_signIn))
            .performClick()
        composeTestRule.waitForIdle()

        // Assert - All login related elements should be visible
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_email))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_password))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_cancel))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_login))
            .assertIsDisplayed()
    }

    @Test
    fun clicking_cancel_button_should_hide_login_fields() = runTest {
        // Setup - Show login fields
        fakeLoginViewModel.setShowLoginFields(true)
        composeTestRule.waitForIdle()

        // Run Test - Click cancel button
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_cancel))
            .performClick()
        composeTestRule.waitForIdle()

        // Assert - Login fields should be hidden
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_email))
            .assertIsNotDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_password))
            .assertIsNotDisplayed()
    }

    @Test
    fun valid_credentials_should_trigger_navigation() = runTest {
        // Setup - Show login fields
        fakeLoginViewModel.setShowLoginFields(true)
        composeTestRule.waitForIdle()

        // Run Test - Enter valid credentials and login
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_email))
            .performTextInput("test@example.com")
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_password))
            .performTextInput("password")
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_login))
            .performClick()

        // Simulate successful authentication
        fakeLoginViewModel.setAuthenticationSucceed(AuthenticateState.AUTHENTICATE)
        composeTestRule.waitForIdle()

        // Assert - Navigation should be triggered
        assert(navigateCalled) { "Navigation was not called after successful login" }
    }

    @Test
    fun invalid_credentials_should_show_error_snack_bar() = runTest {
        // Setup - Show login fields
        fakeLoginViewModel.setShowLoginFields(true)
        composeTestRule.waitForIdle()

        // Run Test - Enter invalid credentials and attempt login
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_email))
            .performTextInput("wrong@example.com")
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_password))
            .performTextInput("wrong")
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_login))
            .performClick()

        // Assert - Error snack_bar should be visible
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_login_error))
            .assertIsDisplayed()
    }

    @Test
    fun loading_state_should_show_progress_indicator() = runTest {
        // Setup - Activate loading state
        fakeLoginViewModel.setLoading(true)
        composeTestRule.waitForIdle()

        // Assert - Loading indicator should be visible and main content hidden
        composeTestRule.onNode(hasTestTag(composeTestRule.activity.getString(R.string.authentication_loading_indicator_tag_test))).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_signIn))
            .assertIsNotDisplayed()
    }

    @Test
    fun network_error_should_show_connection_error_snack_bar() = runTest {
        // Setup - Show login fields and trigger network error
        fakeLoginViewModel.setShowLoginFields(true)
        fakeLoginViewModel.setNetworkError()
        composeTestRule.waitForIdle()

        // Assert - Network error message should be visible
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_internet_connection_error))
            .assertIsDisplayed()
    }

    @Test
    fun empty_email_should_show_empty_email_error_snack_bar() = runTest {
        // Setup - Show login fields and set empty email
        fakeLoginViewModel.setShowLoginFields(true)
        fakeLoginViewModel.setEmail("")
        composeTestRule.waitForIdle()

        // Run Test - Attempt login
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_login))
            .performClick()

        // Assert - Empty email error should be visible
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_empty_email_error))
            .assertIsDisplayed()
    }

    @Test
    fun invalid_email_format_should_show_format_error_snack_bar() = runTest {
        // Setup - Show login fields and set invalid email
        fakeLoginViewModel.setShowLoginFields(true)
        fakeLoginViewModel.setEmail("invalid-email")
        composeTestRule.waitForIdle()

        // Run Test - Attempt login
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_login))
            .performClick()

        // Assert - Invalid email format error should be visible
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_wrong_email_format_error))
            .assertIsDisplayed()
    }

    @Test
    fun empty_password_should_show_empty_password_error_snack_bar() = runTest {
        // Setup - Show login fields and set empty password
        fakeLoginViewModel.setShowLoginFields(true)
        fakeLoginViewModel.setEmail("valid@email.com")
        fakeLoginViewModel.setPassword("")
        composeTestRule.waitForIdle()

        // Run Test - Attempt login
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_login))
            .performClick()

        // Assert - Empty password error should be visible
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_empty_password_error))
            .assertIsDisplayed()
    }

    @Test
    fun version_info_should_be_displayed_correctly() = runTest {
        // Assert - Version text should be visible with correct version
        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.app_version_text_tag_test))
            .assertIsDisplayed()
            .assertTextEquals(expectedVersion)

        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.app_name_header_tag_test))
            .assertIsDisplayed()
            .assertTextEquals(composeTestRule.activity.getString(R.string.app_name))
    }

    @Test
    fun keyboard_options_should_be_correct_for_email_field() = runTest {
        // Setup - Show login fields
        fakeLoginViewModel.setShowLoginFields(true)
        composeTestRule.waitForIdle()

        // Assert - Email field should have correct properties
        composeTestRule.onNode(
            hasImeAction(ImeAction.Done) and
                hasText(composeTestRule.activity.getString(R.string.authentication_email))
        ).assertExists()
    }

    @Test
    fun consecutive_login_attempts_should_work_correctly() = runTest {
        // Setup - Show login fields
        fakeLoginViewModel.setShowLoginFields(true)
        composeTestRule.waitForIdle()

        // Run Test - First failed attempt
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_email))
            .performTextInput("wrong@email.com")
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_password))
            .performTextInput("wrong")
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_login))
            .performClick()
        composeTestRule.waitForIdle()

        // Assert - Error should be shown
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_login_error))
            .assertIsDisplayed()

        // Run Test - Second successful attempt
        fakeLoginViewModel.setEmail("test@example.com")
        fakeLoginViewModel.setPassword("password")
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_login))
            .performClick()
        fakeLoginViewModel.setAuthenticationSucceed(AuthenticateState.AUTHENTICATE)
        composeTestRule.waitForIdle()

        // Assert - Navigation should occur
        assert(navigateCalled)
    }

    @Test
    fun error_snackbar_should_auto_dismiss() = runTest {
        // Setup - Show login fields and error
        fakeLoginViewModel.setShowLoginFields(true)
        fakeLoginViewModel.setDisplayErrorSnackBar(true)
        composeTestRule.waitForIdle()

        // Run Test - Wait for auto dismiss duration
        delay(SnackbarDuration.Short.toMillis())
        composeTestRule.waitForIdle()

        // Assert - Snackbar should not be visible
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_login_error))
            .assertDoesNotExist()
    }

    @Test
    fun multiple_error_states_should_show_correct_messages() = runTest {
        // Setup - Show login fields
        fakeLoginViewModel.setShowLoginFields(true)
        composeTestRule.waitForIdle()

        // Run Test & Assert - Empty email error
        fakeLoginViewModel.setEmail("")
        fakeLoginViewModel.setPassword("password")
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_login))
            .performClick()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_empty_email_error))
            .assertIsDisplayed()

        // Run Test & Assert - Invalid email format
        fakeLoginViewModel.setEmail("invalid-email")
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_login))
            .performClick()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_wrong_email_format_error))
            .assertIsDisplayed()

        // Run Test & Assert - Empty password
        fakeLoginViewModel.setEmail("valid@email.com")
        fakeLoginViewModel.setPassword("")
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_login))
            .performClick()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_empty_password_error))
            .assertIsDisplayed()
    }

    @Test
    fun screen_state_should_reset_when_toggling_login_fields() = runTest {
        // Setup - Show login fields and enter data
        fakeLoginViewModel.setShowLoginFields(true)
        composeTestRule.waitForIdle()

        // Run Test - Enter data and hide fields
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_email))
            .performTextInput("test@email.com")
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_password))
            .performTextInput("password")

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_cancel))
            .performClick()
        composeTestRule.waitForIdle()

        // Show fields again
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_signIn))
            .performClick()
        composeTestRule.waitForIdle()

        // Assert - Fields should be empty
        composeTestRule.onNodeWithText("test@email.com").assertDoesNotExist()
    }

    @Test
    fun loading_state_should_prevent_user_interaction() = runTest {
        // Setup - Show login fields and set loading state
        fakeLoginViewModel.setShowLoginFields(true)
        fakeLoginViewModel.setLoading(true)
        composeTestRule.waitForIdle()

        // Assert - UI elements should not be interactive
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_login))
            .assertDoesNotExist()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.authentication_email))
            .assertDoesNotExist()
    }

    @Test
    fun loading_indicator_should_be_visible_during_authentication() = runTest {
        // Setup - Show login fields
        fakeLoginViewModel.setShowLoginFields(true)
        composeTestRule.waitForIdle()

        // Set loading state
        fakeLoginViewModel.setLoading(true)
        composeTestRule.waitForIdle()

        // Assert - Loading indicator should be visible
        composeTestRule.onNodeWithTag(
            composeTestRule.activity.getString(R.string.authentication_loading_indicator_tag_test)
        ).assertIsDisplayed()
    }

    @Test
    fun version_text_should_be_displayed_with_correct_tag() = runTest {
        composeTestRule.onNodeWithTag(
            composeTestRule.activity.getString(R.string.app_version_text_tag_test)
        ).assertTextEquals(expectedVersion)
    }

    @Test
    fun app_name_header_should_be_displayed_with_correct_tag() = runTest {
        composeTestRule.onNodeWithTag(
            composeTestRule.activity.getString(R.string.app_name_header_tag_test)
        ).assertIsDisplayed()
    }

    @Test
    fun app_name_footer_should_be_displayed_with_correct_tag() = runTest {
        composeTestRule.onNodeWithTag(
            composeTestRule.activity.getString(R.string.app_name_footer_tag_test)
        ).assertIsDisplayed()
    }
}

private fun SnackbarDuration.toMillis(): Long {
    return when (this) {
        SnackbarDuration.Short -> 4000L
        SnackbarDuration.Long -> 10000L
        SnackbarDuration.Indefinite -> Long.MAX_VALUE
    }
}
