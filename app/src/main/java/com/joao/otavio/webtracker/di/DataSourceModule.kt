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
fun interface DataSourceModule {
    @Binds
    @Singleton
    fun bindAuthenticationRemoteDataSource(
        authenticationRemoteDataSourceImpl: AuthenticationRemoteDataSourceImpl
    ) : AuthenticationRemoteDataSource
}
