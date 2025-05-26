package com.joao.otavio.authentication_presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joao.otavio.core.coroutine.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebTrackerLoginViewModel @Inject constructor(
    private val coroutineContextProvider: CoroutineContextProvider
): ViewModel() {

    private val _dummyActionState = MutableStateFlow<String>("Idle")
    val dummyActionState: StateFlow<String> = _dummyActionState

    fun performDummyAction() {
        viewModelScope.launch(coroutineContextProvider.IO) {
            _dummyActionState.value = "Executing..."
            println("Executando ação dummy em IO...")
            delay(DELAY_TIME) // Simula um atraso real
            println("Ação dummy concluída em IO.")
            _dummyActionState.value = "Completed"
        }
    }

    fun performAnotherDummyAction() {
        viewModelScope.launch(coroutineContextProvider.IO) {
            _dummyActionState.value = "Executing..."
            println("Executando ação dummy em IO...")
            delay(DELAY_TIME) // Simula um atraso real
            println("Ação dummy concluída em IO.")
            _dummyActionState.value = "Completed"
        }
    }
}

private const val DELAY_TIME = 5000L
