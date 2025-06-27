package com.joao.otavio.core.di

import com.joao.otavio.core.network.IsNetworkAvailableUseCase
import com.joao.otavio.core.network.IsNetworkAvailableUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
fun interface UseCaseModule {
    @Binds
    @Singleton
    fun bindIsNetworkAvailableUseCase(
        isNetworkAvailableUseCaseImpl: IsNetworkAvailableUseCaseImpl
    ): IsNetworkAvailableUseCase
}
