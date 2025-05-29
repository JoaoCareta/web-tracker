package com.joao.otavio.core.util

fun String?.isNotNullOrEmptyOrBlank(): Boolean {
    return this?.let {
        (this.isNotEmpty()) and (this.isNotBlank())
    } ?: false
}
