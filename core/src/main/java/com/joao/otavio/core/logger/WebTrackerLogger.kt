package com.joao.otavio.core.logger

interface WebTrackerLogger {
    fun i(tag: String, message: String)
    fun w(tag: String, message: String)
    fun e(tag: String, message: String)
    fun e(tag: String, message: String, throwable: Throwable)

    fun interface LogHandler {
        fun log(priority: Int, tag: String, message: String)
    }

    fun setLoggingEnabled(enabled: Boolean)
    fun setLogHandler(handler: LogHandler?)
    fun getTag(): String
}
