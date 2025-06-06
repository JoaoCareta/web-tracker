package com.joao.otavio.authentication_domain.di

import com.joao.otavio.authentication_domain.authentication.FirebaseAuthentication
import com.joao.otavio.authentication_presentation.authentication.Authentication
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
fun interface AuthenticationModule {
    @Binds
    @Singleton
    fun bindFirebaseAuthentication(
        firebaseAuthentication: FirebaseAuthentication
    ) : Authentication
}
