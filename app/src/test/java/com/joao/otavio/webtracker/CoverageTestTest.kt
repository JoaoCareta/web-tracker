package com.joao.otavio.webtracker

import junit.framework.TestCase.assertEquals
import org.junit.Test

class CoverageTestTest {
    val coverageTest = CoverageTest()

    @Test
    fun soma() {
        val a = 1
        val b = 2

        val result = coverageTest.soma(a, b)

        assertEquals(result, (a+b))
    }

    @Test
    fun divisao() {
        val a = 4
        val b = 2

        val result = coverageTest.divisao(a, b)

        assertEquals(result, a/b)
    }

    @Test
    fun divisaoComZero() {
        val a = 4
        val b = 0

        val result = coverageTest.divisao(a, b)

        assertEquals(result, null)
    }
}