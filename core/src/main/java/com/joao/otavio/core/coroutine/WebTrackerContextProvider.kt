package com.joao.otavio.core.coroutine

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class WebTrackerContextProvider @Inject constructor() : CoroutineContextProvider {
    override val Main: CoroutineContext by lazy { Dispatchers.Main }
    override val IO: CoroutineContext by lazy { Dispatchers.IO }
    override val Default: CoroutineContext by lazy { Dispatchers.Default }
}
