package com.joao.otavio.webtracker

class CoverageClass {
    fun soma(a: Int, b: Int): Int {
        return a+b
    }

    fun divisao(a: Int, b: Int): Int? {
        return if (b == 0){
            null
        } else a/b
    }
}
