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
fun interface CoroutineContextProviderModule {

    @Binds
    @Singleton
    fun bindsCoroutineContextProvider(
        webTrackerProvider: WebTrackerContextProvider
    ): CoroutineContextProvider
}
