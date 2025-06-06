package com.joao.otavio.authentication_domain.di

import com.joao.otavio.authentication_domain.usecases.AuthenticateUserUseCaseImpl
import com.joao.otavio.authentication_domain.usecases.CheckOrganizationLoginStatusUseCaseImpl
import com.joao.otavio.authentication_domain.usecases.SaveOrganizationUseCaseImpl
import com.joao.otavio.authentication_presentation.usecases.AuthenticateUserUseCase
import com.joao.otavio.authentication_presentation.usecases.CheckOrganizationLoginStatusUseCase
import com.joao.otavio.authentication_presentation.usecases.SaveOrganizationUseCase
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
        checkUserLoginStatusUseCaseImpl: CheckOrganizationLoginStatusUseCaseImpl
    ): CheckOrganizationLoginStatusUseCase

    @Binds
    @Singleton
    fun bindSaveOrganizationUseCase(
        saveOrganizationUseCaseImpl: SaveOrganizationUseCaseImpl
    ) : SaveOrganizationUseCase
}
