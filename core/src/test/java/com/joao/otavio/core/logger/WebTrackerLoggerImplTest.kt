package com.joao.otavio.core.logger

import android.util.Log
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test

class WebTrackerLoggerImplTest {

    private lateinit var loggerImpl: WebTrackerLoggerImpl
    private lateinit var mockLogHandler: WebTrackerLogger.LogHandler

    @Before
    fun setUp() {
        // Mockk
        mockkStatic(Log::class)
        mockLogHandler = mockk(relaxed = true)
        loggerImpl = WebTrackerLoggerImpl()
        loggerImpl.setLogHandler(mockLogHandler)
        loggerImpl.setLoggingEnabled(true)
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.w(any(), any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        // Mockk
        unmockkStatic(Log::class)
    }

    @Test
    fun `given logging enabled is false, when i is called, then Log_i and logHandler_log should not be called`() {
        // Mockk
        loggerImpl.setLoggingEnabled(false)

        // Run Test
        loggerImpl.i(TEST_TAG, TEST_MESSAGE)

        // Assert
        verify(exactly = 0) { Log.i(TEST_TAG, TEST_MESSAGE) }
        verify(exactly = 0) { mockLogHandler.log(Log.INFO, TEST_TAG, TEST_MESSAGE) }
    }

    @Test
    fun `given logging enabled is true, when i is called, then Log_i and logHandler_log should be called`() {
        // Run Test
        loggerImpl.i(TEST_TAG, TEST_MESSAGE)

        // Assert
        verify { Log.i(TEST_TAG, TEST_MESSAGE) }
        verify { mockLogHandler.log(Log.INFO, TEST_TAG, TEST_MESSAGE) }
    }

    @Test
    fun `given a new logHandler, when setLogHandler is called, then the internal logHandler should be updated`() {
        // Mockk
        val newMockLogHandler = mockk<WebTrackerLogger.LogHandler>(relaxed = true)

        // Run Test
        loggerImpl.setLogHandler(newMockLogHandler)
        loggerImpl.i(TEST_TAG, TEST_MESSAGE)

        // Assert
        verify(exactly = 0) { mockLogHandler.log(any(), any(), any()) }
        verify { newMockLogHandler.log(Log.INFO, TEST_TAG, TEST_MESSAGE) }
    }

    @Test
    fun `given null logHandler, when setLogHandler is called, then the internal logHandler should be null and Log_i should still be called`() {
        // Mockk
        loggerImpl.setLogHandler(null)

        // Run Test
        loggerImpl.i(TEST_TAG, TEST_MESSAGE)

        // Assert
        verify { Log.i(TEST_TAG, TEST_MESSAGE) }
        verify(exactly = 0) { mockLogHandler.log(any(), any(), any()) }
    }

    @Test
    fun `when getTag is called, then it should return a tag with the correct prefix and calling class name`() {
        // Mockk
        val mockThrowable = mockk<Throwable>()
        val mockStackTraceElement = mockk<StackTraceElement>()
        every { mockThrowable.stackTrace } returns arrayOf(mockk(), mockStackTraceElement)
        every { mockStackTraceElement.className } returns TEST_CLASS_NAME

        // Run Test
        val getTagMethod = WebTrackerLoggerImpl::class.java.getDeclaredMethod("getTag")
        getTagMethod.isAccessible = true
        val generatedTag = getTagMethod.invoke(loggerImpl) as String

        // Assert
        assertNotNull(generatedTag)
    }

    @Test
    fun `given logging enabled is true, when w is called, then Log_w and logHandler_log should be called`() {
        // Run Test
        loggerImpl.w(TEST_TAG, TEST_MESSAGE)

        // Assert
        verify { Log.w(TEST_TAG, TEST_MESSAGE) }
        verify { mockLogHandler.log(Log.WARN, TEST_TAG, TEST_MESSAGE) }
    }

    @Test
    fun `given logging enabled is true, when e with message is called, then Log_e and logHandler_log should be called`() {
        // Run Test
        loggerImpl.e(TEST_TAG, TEST_MESSAGE)

        // Assert
        verify { Log.e(TEST_TAG, TEST_MESSAGE) }
        verify { mockLogHandler.log(Log.ERROR, TEST_TAG, TEST_MESSAGE) }
    }

    @Test
    fun `given logging enabled is true, when e with message and throwable is called, then Log_e and logHandler_log should be called with stack trace`() {
        // Mockk
        val throwable = Throwable(TEST_EXCEPTION_MESSAGE)
        val expectedLogMessage = "$TEST_MESSAGE\n${Log.getStackTraceString(throwable)}"

        // Run Test
        loggerImpl.e(TEST_TAG, TEST_MESSAGE, throwable)

        // Assert
        verify { Log.e(TEST_TAG, TEST_MESSAGE, throwable) }
        verify { mockLogHandler.log(Log.ERROR, TEST_TAG, expectedLogMessage) }
    }

    @Test
    fun `given logging enabled is false, when w is called, then Log_w and logHandler_log should not be called`() {
        // Mockk
        loggerImpl.setLoggingEnabled(false)

        // Run Test
        loggerImpl.w(TEST_TAG, TEST_MESSAGE)

        // Assert
        verify(exactly = 0) { Log.w(TEST_TAG, TEST_MESSAGE) }
        verify(exactly = 0) { mockLogHandler.log(Log.WARN, TEST_TAG, TEST_MESSAGE) }
    }

    @Test
    fun `given logging enabled is false, when e with message is called, then Log_e and logHandler_log should not be called`() {
        // Mockk
        loggerImpl.setLoggingEnabled(false)

        // Run Test
        loggerImpl.e(TEST_TAG, TEST_MESSAGE)

        // Assert
        verify(exactly = 0) { Log.e(TEST_TAG, TEST_MESSAGE) }
        verify(exactly = 0) { mockLogHandler.log(Log.ERROR, TEST_TAG, TEST_MESSAGE) }
    }

    @Test
    fun `given logging enabled is false, when e with message and throwable is called, then Log_e and logHandler_log should not be called`() {
        // Mockk
        loggerImpl.setLoggingEnabled(false)
        val throwable = Throwable(TEST_EXCEPTION_MESSAGE)

        // Run Test
        loggerImpl.e(TEST_TAG, TEST_MESSAGE, throwable)

        // Assert
        verify(exactly = 0) { Log.e(TEST_TAG, TEST_MESSAGE, throwable) }
        verify(exactly = 0) { mockLogHandler.log(Log.ERROR, TEST_TAG, "$TEST_MESSAGE\n${Log.getStackTraceString(throwable)}") }
    }

    companion object {
        const val TEST_TAG = "TestTag"
        const val TEST_MESSAGE = "Test Message"
        const val TEST_EXCEPTION_MESSAGE = "Test Exception"
        const val TEST_CLASS_NAME = "com.example.MyCallingClass"
        const val EXPECTED_TAG = "WebTrackerApp_DirectMethodHandleAccessor"
    }
}
