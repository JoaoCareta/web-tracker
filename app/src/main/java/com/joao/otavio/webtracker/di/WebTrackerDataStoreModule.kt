package com.joao.otavio.webtracker.di

import com.joao.otavio.core.datastore.WebTrackerDataStore
import com.joao.otavio.core.datastore.WebTrackerDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
fun interface WebTrackerDataStoreModule {
    @Binds
    @Singleton
    fun bindWebTrackerDataStore(
        webTrackerDataStoreImpl: WebTrackerDataStoreImpl
    ): WebTrackerDataStore
}
