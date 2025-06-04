package com.joao.otavio.core.util

import junit.framework.TestCase.assertEquals
import org.junit.Test
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class TimeUtilsTest {
    @Test
    fun `verify ONE_SECOND constant value`() {
        // Mockk
        val expectedOneSecond = 1.0.toDuration(DurationUnit.SECONDS)

        // Run Test
        val result = TimeUtils.ONE_SECOND

        // Assert
        assertEquals(expectedOneSecond, result)
    }

    @Test
    fun `verify ONE_MINUTE constant value`() {
        // Mockk
        val expectedOneMinute = 1.0.toDuration(DurationUnit.MINUTES)

        // Run Test
        val result = TimeUtils.ONE_MINUTE

        // Assert
        assertEquals(expectedOneMinute, result)
    }

    @Test
    fun `verify ONE_MINUTE equals 60 seconds`() {
        // Mockk
        val sixtySeconds = 60.0.toDuration(DurationUnit.SECONDS)

        // Run Test
        val result = TimeUtils.ONE_MINUTE

        // Assert
        assertEquals(sixtySeconds, result)
    }
}
