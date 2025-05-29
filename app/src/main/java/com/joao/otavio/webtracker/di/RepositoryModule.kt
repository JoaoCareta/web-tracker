package com.joao.otavio.webtracker.di

import com.joao.otavio.authentication_domain.repository.AuthenticationRepositoryImpl
import com.joao.otavio.authentication_presentation.repository.AuthenticationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
fun interface RepositoryModule {
    @Binds
    @Singleton
    fun bindAuthenticationRepository(
        authenticationRepositoryImpl: AuthenticationRepositoryImpl
    ): AuthenticationRepository
}
