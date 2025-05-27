package com.joao.otavio.authentication_domain.firebase

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnCanceledListener
import io.mockk.slot

class FirebaseAuthenticationTest {
    private val firebaseAuth: FirebaseAuth = mockk()
    private val mockedTask = mockk<Task<AuthResult>>()
    private val mockedAuthResult = mockk<AuthResult>()
    private val firebaseAuthentication = FirebaseAuthentication(firebaseAuth)

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.w(any(), any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `given valid credentials when login succeeds then return true`() = runTest {
        // Mockk
        val successSlot = slot<OnSuccessListener<AuthResult>>()
        val failureSlot = slot<OnFailureListener>()
        val cancelSlot = slot<OnCanceledListener>()

        every { firebaseAuth.signInWithEmailAndPassword(EMAIL, PASSWORD) } returns mockedTask
        every { mockedTask.addOnSuccessListener(capture(successSlot)) } answers {
            successSlot.captured.onSuccess(mockedAuthResult)
            mockedTask
        }
        every { mockedTask.addOnFailureListener(capture(failureSlot)) } returns mockedTask
        every { mockedTask.addOnCanceledListener(capture(cancelSlot)) } returns mockedTask
        every { mockedAuthResult.user?.uid } returns "mock-uid"

        // Run Test
        val result = firebaseAuthentication.loginUserWithEmailAndPassword(EMAIL, PASSWORD)

        // Assert
        assertTrue(result)
    }

    @Test
    fun `given valid credentials when login fails then return false`() = runTest {
        // Mockk
        val successSlot = slot<OnSuccessListener<AuthResult>>()
        val failureSlot = slot<OnFailureListener>()
        val cancelSlot = slot<OnCanceledListener>()

        every { firebaseAuth.signInWithEmailAndPassword(EMAIL, PASSWORD) } returns mockedTask
        every { mockedTask.addOnSuccessListener(capture(successSlot)) } returns mockedTask
        every { mockedTask.addOnFailureListener(capture(failureSlot)) } answers {
            failureSlot.captured.onFailure(Exception("Login failed"))
            mockedTask
        }
        every { mockedTask.addOnCanceledListener(capture(cancelSlot)) } returns mockedTask

        // Run Test
        val result = firebaseAuthentication.loginUserWithEmailAndPassword(EMAIL, PASSWORD)

        // Assert
        assertFalse(result)
    }

    @Test
    fun `given valid credentials when login is cancelled then return false`() = runTest {
        // Mockk
        val successSlot = slot<OnSuccessListener<AuthResult>>()
        val failureSlot = slot<OnFailureListener>()
        val cancelSlot = slot<OnCanceledListener>()

        every { firebaseAuth.signInWithEmailAndPassword(EMAIL, PASSWORD) } returns mockedTask
        every { mockedTask.addOnSuccessListener(capture(successSlot)) } returns mockedTask
        every { mockedTask.addOnFailureListener(capture(failureSlot)) } returns mockedTask
        every { mockedTask.addOnCanceledListener(capture(cancelSlot)) } answers {
            cancelSlot.captured.onCanceled()
            mockedTask
        }

        // Run Test
        val result = firebaseAuthentication.loginUserWithEmailAndPassword(EMAIL, PASSWORD)

        // Assert
        assertFalse(result)
    }

    @Test
    fun `given valid credentials when exception occurs then return false`() = runTest {
        // Mockk
        every { firebaseAuth.signInWithEmailAndPassword(EMAIL, PASSWORD) } throws Exception("Unexpected error")

        // Run Test
        val result = firebaseAuthentication.loginUserWithEmailAndPassword(EMAIL, PASSWORD)

        // Assert
        assertFalse(result)
    }

    companion object {
        const val EMAIL = "test@test.com"
        const val PASSWORD = "123456"
    }
}
