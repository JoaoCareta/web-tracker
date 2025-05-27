package com.joao.otavio.webtracker.di

import com.joao.otavio.authentication_domain.datasource.AuthenticationRemoteDataSourceImpl
import com.joao.otavio.authentication_presentation.datasource.AuthenticationRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindAuthenticationRemoteDataSource(
        authenticationRemoteDataSourceImpl: AuthenticationRemoteDataSourceImpl
    ) : AuthenticationRemoteDataSource
}
