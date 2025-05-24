@file:OptIn(ExperimentalCoroutinesApi::class)
package com.joao.otavio.core.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlin.coroutines.CoroutineContext

class TestContextProvider(
    private val testDispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
): CoroutineContextProvider {
    override val Main: CoroutineContext by lazy { testDispatcher }
    override val IO: CoroutineContext by lazy { testDispatcher }
    override val Default: CoroutineContext by lazy { testDispatcher }
}
