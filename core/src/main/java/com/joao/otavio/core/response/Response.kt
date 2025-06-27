package com.joao.otavio.core.response

sealed interface Response<out T> {
    data class Success<T>(val data: T) : Response<T>
    data class Error<T>(val message: String, val errorCode: Int? = null) : Response<T>
}
