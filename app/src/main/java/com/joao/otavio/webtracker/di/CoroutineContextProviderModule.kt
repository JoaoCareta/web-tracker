package com.joao.otavio.webtracker.di

import com.joao.otavio.core.coroutine.CoroutineContextProvider
import com.joao.otavio.core.coroutine.WebTrackerContextProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoroutineContextProviderModule {

    @Binds
    @Singleton
    abstract fun bindsCoroutineContextProvider(
        webTrackerProvider: WebTrackerContextProvider
    ): CoroutineContextProvider
}
