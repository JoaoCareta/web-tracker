package com.joao.otavio.webtracker.di

import com.joao.otavio.authentication_domain.usecases.AuthenticateUserUseCaseImpl
import com.joao.otavio.authentication_domain.usecases.CheckUserLoginStatusUseCaseImpl
import com.joao.otavio.authentication_presentation.usecases.AuthenticateUserUseCase
import com.joao.otavio.authentication_presentation.usecases.CheckUserLoginStatusUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {
    @Binds
    @Singleton
    fun bindAuthenticateUserUseCase(
        authenticateUserUseCaseImpl: AuthenticateUserUseCaseImpl
    ): AuthenticateUserUseCase

    @Binds
    @Singleton
    fun bindCheckUserLoginStatusUseCase(
        checkUserLoginStatusUseCaseImpl: CheckUserLoginStatusUseCaseImpl
    ): CheckUserLoginStatusUseCase
}
