import com.joao.otavio.authentication_presentation.utils.MainDispatcherRule
import com.joao.otavio.authentication_presentation.viewmodel.WebTrackerLoginViewModel
import com.joao.otavio.core.coroutine.CoroutineContextProvider
import com.joao.otavio.core.coroutine.TestContextProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WebTrackerLoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var testContextProvider: CoroutineContextProvider
    private lateinit var viewModel: WebTrackerLoginViewModel

    @Before
    fun setup() {
        testContextProvider = TestContextProvider(mainDispatcherRule.testDispatcher)
        viewModel = WebTrackerLoginViewModel(testContextProvider)
    }

    @Test
    fun `performDummyAction should update state to Executing and then Completed`() = runTest {
        // Initial Assert
        assertEquals("Idle", viewModel.dummyActionState.value)

        // Test
        viewModel.performDummyAction()

        // Assert
        assertEquals("Executing...", viewModel.dummyActionState.value)
        advanceUntilIdle()
        assertEquals("Completed", viewModel.dummyActionState.value)
    }

    @Test
    fun `performAnotherDummyAction should update state to Executing and then Completed`() = runTest {
        // Initial Assert
        assertEquals("Idle", viewModel.dummyActionState.value)

        // Test
        viewModel.performAnotherDummyAction()

        // Assert
        assertEquals("Executing...", viewModel.dummyActionState.value)
        advanceUntilIdle()
        assertEquals("Completed", viewModel.dummyActionState.value)
    }
}