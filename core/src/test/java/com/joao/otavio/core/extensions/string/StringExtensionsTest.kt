package com.joao.otavio.core.extensions.string

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class StringExtensionsTest {

    @Test
    fun `given a null string, when call isNotNullOrEmptyOrBlank, then should return false`() {
        // Mockk
        val stringMocked: String? = null

        // Run
        val result = stringMocked.isNotNullOrEmptyOrBlank()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `given a blank string, when call isNotNullOrEmptyOrBlank, then should return false`() {
        // Mockk
        val stringMocked = BLANK_STRING

        // Run
        val result = stringMocked.isNotNullOrEmptyOrBlank()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `given a empty string, when call isNotNullOrEmptyOrBlank, then should return false`() {
        // Mockk
        val stringMocked = EMPTY_STRING

        // Run
        val result = stringMocked.isNotNullOrEmptyOrBlank()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `given a regular string, when call isNotNullOrEmptyOrBlank, then should return true`() {
        // Mockk
        val stringMocked = REGULAR_STRING

        // Run
        val result = stringMocked.isNotNullOrEmptyOrBlank()

        // Assert
        assertTrue(result)
    }

    @Test
    fun `when email is valid then return true`() {
        // Mockk
        val validEmails = listOf(
            "test@example.com",
            "test.name@example.com",
            "test+name@example.com",
            "test@subdomain.example.com",
            "test@example-domain.com",
            "123@example.com",
            "test@example.co.uk",
            "test.name+filter@example.com",
            "user123@domain456.com",
            "first.last@example.com"
        )

        // Assert
        validEmails.forEach { email ->
            assertTrue("Email $email should be valid", email.isValidEmail())
        }
    }

    @Test
    fun `when email is invalid then return false`() {
        // Mockk
        val invalidEmails = listOf(
            "test@me%.com",
            "test@%me.com",
            "test@me%^,com",
            "test@me,com",
        )

        // Assert
        invalidEmails.forEach { email ->
            assertFalse("Email $email should be invalid", email.isValidEmail())
        }
    }

    @Test
    fun `when email has special characters in valid positions then return true`() {
        // Mockk
        val validEmailsWithSpecialChars = listOf(
            "test-name@example.com",
            "test_name@example.com",
            "test.name@example.com",
            "test+filter@example.com"
        )

        // Assert
        validEmailsWithSpecialChars.forEach { email ->
            assertTrue("Email $email should be valid", email.isValidEmail())
        }
    }

    @Test
    fun `when email has correct case sensitivity then return true`() {
        // Mockk
        val validEmailsWithCases = listOf(
            "TEST@EXAMPLE.COM",
            "test@example.com",
            "Test@Example.com",
            "TeSt@eXaMpLe.CoM"
        )

        // Assert
        validEmailsWithCases.forEach { email ->
            assertTrue("Email $email should be valid", email.isValidEmail())
        }
    }

    @Test
    fun `given a blank string, when call isEmptyOrBlank, then should return true`() {
        // Mockk
        val stringMocked = BLANK_STRING

        // Run
        val result = stringMocked.isEmptyOrBlank()

        // Assert
        assertTrue(result)
    }

    @Test
    fun `given an empty string, when call isEmptyOrBlank, then should return true`() {
        // Mockk
        val stringMocked = EMPTY_STRING

        // Run
        val result = stringMocked.isEmptyOrBlank()

        // Assert
        assertTrue(result)
    }

    @Test
    fun `given a regular string, when call isEmptyOrBlank, then should return false`() {
        // Mockk
        val stringMocked = REGULAR_STRING

        // Run
        val result = stringMocked.isEmptyOrBlank()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `given a blank string, when call isNotEmptyOrBlank, then should return false`() {
        // Mockk
        val stringMocked = BLANK_STRING

        // Run
        val result = stringMocked.isNotEmptyOrBlank()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `given an empty string, when call isNotEmptyOrBlank, then should return false`() {
        // Mockk
        val stringMocked = EMPTY_STRING

        // Run
        val result = stringMocked.isNotEmptyOrBlank()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `given a regular string, when call isNotEmptyOrBlank, then should return true`() {
        // Mockk
        val stringMocked = REGULAR_STRING

        // Run
        val result = stringMocked.isNotEmptyOrBlank()

        // Assert
        assertTrue(result)
    }

    @Test
    fun `given a string with only spaces, when call isEmptyOrBlank, then should return true`() {
        // Mockk
        val stringMocked = "   "

        // Run
        val result = stringMocked.isEmptyOrBlank()

        // Assert
        assertTrue(result)
    }

    @Test
    fun `given a string with only spaces, when call isNotEmptyOrBlank, then should return false`() {
        // Mockk
        val stringMocked = "   "

        // Run
        val result = stringMocked.isNotEmptyOrBlank()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `given a string with spaces and characters, when call isEmptyOrBlank, then should return false`() {
        // Mockk
        val stringMocked = " hello "

        // Run
        val result = stringMocked.isEmptyOrBlank()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `given a string with spaces and characters, when call isNotEmptyOrBlank, then should return true`() {
        // Mockk
        val stringMocked = " hello "

        // Run
        val result = stringMocked.isNotEmptyOrBlank()

        // Assert
        assertTrue(result)
    }

    companion object {
        const val BLANK_STRING = " "
        const val EMPTY_STRING = ""
        const val REGULAR_STRING = "string"
    }
}
