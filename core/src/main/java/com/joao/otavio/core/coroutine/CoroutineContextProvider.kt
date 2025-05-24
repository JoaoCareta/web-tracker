package com.joao.otavio.core.coroutine

import kotlin.coroutines.CoroutineContext

interface CoroutineContextProvider {
    val Main: CoroutineContext
    val IO: CoroutineContext
    val Default: CoroutineContext
}
