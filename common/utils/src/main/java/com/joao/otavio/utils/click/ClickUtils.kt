package com.joao.otavio.utils.click

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember

object ClickUtils {
    private var mLastClick = mutableLongStateOf(0L)

    @Composable
    fun DoIfCanClick(
        interval: Long = 300L,
        content: @Composable () -> Unit
    ) {
        val canClick = remember {
            { System.currentTimeMillis() - mLastClick.longValue > interval }
        }

        val updateLastClick = remember {
            { mLastClick.longValue = System.currentTimeMillis() }
        }

        if (canClick()) {
            updateLastClick()
            content()
        }
    }


    fun doIfCanClick(interval: Long = 300L, action: () -> Unit) {
        if (System.currentTimeMillis() - mLastClick.longValue > interval) {
            mLastClick.longValue = System.currentTimeMillis()
            action()
        }
    }
}
