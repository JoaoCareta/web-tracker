package com.joao.otavio.core.extensions.longs

import android.annotation.SuppressLint
import com.joao.otavio.core.util.TimeUtils.ONE_MINUTE
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@SuppressLint("DefaultLocale")
fun Long.formatToMinutesAndSeconds(): String {
    val totalSeconds = this.toDuration(DurationUnit.MILLISECONDS).inWholeSeconds
    val minutes = totalSeconds.toDuration(DurationUnit.SECONDS).inWholeMinutes
    val seconds = totalSeconds % ONE_MINUTE.inWholeSeconds
    return String.format("%02d:%02d", minutes, seconds)
}
