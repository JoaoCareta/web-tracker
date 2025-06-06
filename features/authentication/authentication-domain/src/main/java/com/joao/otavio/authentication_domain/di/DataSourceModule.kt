package com.joao.otavio.authentication_domain.di

import com.joao.otavio.authentication_domain.datasource.AuthenticationLocalDataSourceImpl
import com.joao.otavio.authentication_domain.datasource.AuthenticationRemoteDataSourceImpl
import com.joao.otavio.authentication_presentation.datasource.AuthenticationLocalDataSource
import com.joao.otavio.authentication_presentation.datasource.AuthenticationRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {
    @Binds
    @Singleton
    fun bindAuthenticationRemoteDataSource(
        authenticationRemoteDataSourceImpl: AuthenticationRemoteDataSourceImpl
    ) : AuthenticationRemoteDataSource

    @Binds
    @Singleton
    fun bindAuthenticationLocalDataSource(
        authenticationLocalDataSource: AuthenticationLocalDataSourceImpl
    ): AuthenticationLocalDataSource
}
