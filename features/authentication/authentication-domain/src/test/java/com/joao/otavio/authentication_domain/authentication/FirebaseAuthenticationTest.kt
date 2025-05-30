package com.joao.otavio.authentication_domain.authentication

import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.joao.otavio.core.logger.WebTrackerLogger
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FirebaseAuthenticationTest {
    private val firebaseAuth: FirebaseAuth = mockk()
    private val mockedTask = mockk<Task<AuthResult>>()
    private val mockedAuthResult = mockk<AuthResult>()
    private val logger: WebTrackerLogger = mockk()
    private val firebaseAuthentication = FirebaseAuthentication(
        firebaseAuth = firebaseAuth,
        logger = logger
    )

    @Before
    fun setUp() {
        every { logger.getTag() } returns CLASS_NAME
        justRun { logger.i(any(), any()) }
        justRun { logger.w(any(), any()) }
        justRun { logger.e(any(), any(), any()) }
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
    fun `given valid credentials when login succeeds but the current user is null then return true`() = runTest {
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
        every { mockedAuthResult.user } returns null

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

    @Test
    fun `given a logged user, when authentication tries to get the currentUser and everything went well, then it should return it`() = runTest {
        // Mockk
        every { firebaseAuth.currentUser?.uid } returns USER_ID

        // Run Test
        val result = firebaseAuthentication.getLoginUserId()

        // Assert
        assertEquals(result, USER_ID)
    }

    @Test
    fun `given a logged user, when authentication tries to get the currentUser and it returns null, then it should return null`() = runTest {
        // Mockk
        every { firebaseAuth.currentUser?.uid } returns null

        // Run Test
        val result = firebaseAuthentication.getLoginUserId()

        // Assert
        assertNull(result)
    }

    @Test
    fun `given a logged user, when authentication tries to get the currentUser and it is null, then it should return null`() = runTest {
        // Mockk
        every { firebaseAuth.currentUser } returns null

        // Run Test
        val result = firebaseAuthentication.getLoginUserId()

        // Assert
        assertNull(result)
    }

    @Test
    fun `given a logged user, when authentication tries to get the currentUser and throws and exception, then it should return null`() = runTest {
        // Mockk
        every { firebaseAuth.currentUser?.uid } throws Exception()

        // Run Test
        val result = firebaseAuthentication.getLoginUserId()

        // Assert
        assertNull(result)
    }

    companion object {
        const val EMAIL = "test@test.com"
        const val PASSWORD = "123456"
        const val USER_ID = "user_id"
        const val CLASS_NAME = "FirebaseAuthentication"
    }
}
