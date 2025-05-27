package dependencies.testing

object Testing {
    private const val COROUTINE_TEST_VERSION = "1.8.0"
    const val COROUTINE_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$COROUTINE_TEST_VERSION"
    private const val MOCKK_VERSION = "1.13.8"
    const val MOCKK = "io.mockk:mockk:$MOCKK_VERSION"
    const val MOCKK_ANDROID = "io.mockk:mockk-android:$MOCKK_VERSION"
}
