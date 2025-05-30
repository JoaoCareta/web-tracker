package com.joao.otavio.webtracker.di

import com.joao.otavio.core.logger.WebTrackerLogger
import com.joao.otavio.core.logger.WebTrackerLoggerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
fun interface LoggerModule {
    @Binds
    @Singleton
    fun bindWebTrackerLogger(
        webTrackerLoggerImpl: WebTrackerLoggerImpl
    ): WebTrackerLogger
}
