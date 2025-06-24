package com.joao.otavio.core.util

import androidx.core.util.PatternsCompat

fun String?.isNotNullOrEmptyOrBlank(): Boolean {
    return this?.let {
        (this.isNotEmpty()) and (this.isNotBlank())
    } ?: false
}

fun String.isEmptyOrBlank(): Boolean {
    return (this.isEmpty()) or (this.isBlank())
}

fun String.isValidEmail(): Boolean {
    return PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()
}
