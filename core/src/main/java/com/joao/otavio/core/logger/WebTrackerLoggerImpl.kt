package com.joao.otavio.core.logger

import android.util.Log
import javax.inject.Inject

class WebTrackerLoggerImpl @Inject constructor() : WebTrackerLogger  {
    private var loggingEnabled = true
    private var logHandler: WebTrackerLogger.LogHandler? = null
    private var tagPrefix: String = "WebTrackerApp_"

    override fun setLoggingEnabled(enabled: Boolean) {
        loggingEnabled = enabled
    }

    override fun setLogHandler(handler: WebTrackerLogger.LogHandler?) {
        logHandler = handler
    }

    override fun getTag(): String {
        return generateTag(Throwable(message = "Getting the className").stackTrace[1].className)
    }

    private fun generateTag(callerClassName: String): String {
        val simpleClassName = callerClassName.substringAfterLast(".")
        return "$tagPrefix$simpleClassName"
    }

    override fun i(tag: String, message: String) {
        if (loggingEnabled) {
            Log.i(tag, message)
            logHandler?.log(Log.INFO, tag, message)
        }
    }

    override fun w(tag: String, message: String) {
        if (loggingEnabled) {
            Log.w(tag, message)
            logHandler?.log(Log.WARN, tag, message)
        }
    }

    override fun e(tag: String, message: String) {
        if (loggingEnabled) {
            Log.e(tag, message)
            logHandler?.log(Log.ERROR, tag, message)
        }
    }

    override fun e(tag: String, message: String, throwable: Throwable) {
        if (loggingEnabled) {
            Log.e(tag, message, throwable)
            logHandler?.log(Log.ERROR, tag, "$message\n${Log.getStackTraceString(throwable)}")
        }
    }
}
