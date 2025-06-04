package com.joao.otavio.core.util

import kotlin.time.DurationUnit
import kotlin.time.toDuration

object TimeUtils {
    val ONE_SECOND = 1.0.toDuration(DurationUnit.SECONDS)
    val ONE_MINUTE = 1.0.toDuration(DurationUnit.MINUTES)
}
