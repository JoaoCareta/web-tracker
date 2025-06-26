package com.joao.otavio.authentication_presentation.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A JUnit [TestRule] que define o Main dispatcher para [testDispatcher]
 * durante a duração do teste.
 *
 * Use com:
 * ```
 * @get:Rule
 * val mainDispatcherRule = MainDispatcherRule()
 * ```
 */
@ExperimentalCoroutinesApi // Anotação necessária porque TestDispatcher e related APIs são experimentais
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(), // Ou StandardTestDispatcher()
) : TestWatcher() { // Extende TestWatcher para ter acesso aos callbacks do ciclo de vida do teste
    override fun starting(description: Description) {
        // Chamado antes de cada teste. Define o Dispatchers.Main para o nosso TestDispatcher.
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        // Chamado depois de cada teste. Reseta o Dispatchers.Main para o original.
        Dispatchers.resetMain()
    }
}