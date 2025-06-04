package com.joao.otavio.core.util

import com.joao.otavio.core.util.formatToMinutesAndSeconds
import junit.framework.TestCase.assertEquals
import org.junit.Test

class LongExtensions {
    @Test
    fun `when time is zero then return 00_00`() {
        // Mockk
        val timeInMillis = 0L
        val expected = "00:00"

        // Run Test
        val result = timeInMillis.formatToMinutesAndSeconds()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `when time is less than one minute then format correctly`() {
        // Mockk
        val testCases = mapOf(
            1000L to "00:01",
            30000L to "00:30",
            59000L to "00:59"
        )

        // Run Test & Assert
        testCases.forEach { (input, expected) ->
            assertEquals(
                "Failed for input: $input",
                expected,
                input.formatToMinutesAndSeconds()
            )
        }
    }

    @Test
    fun `when time is exactly one minute then return 01_00`() {
        // Mockk
        val oneMinuteInMillis = 60000L
        val expected = "01:00"

        // Run Test
        val result = oneMinuteInMillis.formatToMinutesAndSeconds()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `when time is more than one minute then format correctly`() {
        // Mockk
        val testCases = mapOf(
            61000L to "01:01",
            90000L to "01:30",
            119000L to "01:59",
            120000L to "02:00",
            3599000L to "59:59"
        )

        // Run Test & Assert
        testCases.forEach { (input, expected) ->
            assertEquals(
                "Failed for input: $input",
                expected,
                input.formatToMinutesAndSeconds()
            )
        }
    }

    @Test
    fun `when time has milliseconds then round down to nearest second`() {
        // Mockk
        val testCases = mapOf(
            1001L to "00:01",
            1999L to "00:01",
            59999L to "00:59"
        )

        // Run Test & Assert
        testCases.forEach { (input, expected) ->
            assertEquals(
                "Failed for input: $input",
                expected,
                input.formatToMinutesAndSeconds()
            )
        }
    }

    @Test
    fun `when time is large value then format correctly`() {
        // Mockk
        val testCases = mapOf(
            3600000L to "60:00",
            7200000L to "120:00",
            7323000L to "122:03"
        )

        // Run Test & Assert
        testCases.forEach { (input, expected) ->
            assertEquals(
                "Failed for input: $input",
                expected,
                input.formatToMinutesAndSeconds()
            )
        }
    }

    @Test
    fun `when time has boundary values then format correctly`() {
        // Mockk
        val testCases = mapOf(
            999L to "00:00",
            1000L to "00:01",
            59999L to "00:59",
            60000L to "01:00",
            3599999L to "59:59"
        )

        // Run Test & Assert
        testCases.forEach { (input, expected) ->
            assertEquals(
                "Failed for input: $input",
                expected,
                input.formatToMinutesAndSeconds()
            )
        }
    }
}
