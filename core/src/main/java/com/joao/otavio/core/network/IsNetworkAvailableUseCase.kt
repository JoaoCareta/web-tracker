package com.joao.otavio.core.network

fun interface IsNetworkAvailableUseCase {
    operator fun invoke() : Boolean
}
