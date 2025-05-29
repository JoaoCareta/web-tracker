package com.joao.otavio.core.datastore.util

import com.joao.otavio.core.util.isNotNullOrEmptyOrBlank
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

    companion object {
        const val BLANK_STRING = " "
        const val EMPTY_STRING = ""
        const val REGULAR_STRING = "string"
    }
}
