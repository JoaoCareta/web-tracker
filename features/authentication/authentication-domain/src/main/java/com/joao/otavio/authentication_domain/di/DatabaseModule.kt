package com.joao.otavio.authentication_domain.di

import com.joao.otavio.authentication_data.database.OrganizationDao
import com.joao.otavio.core.databse.WebTrackerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideOrganizationDao(
        webTrackerDatabase: WebTrackerDatabase
    ) : OrganizationDao {
        return webTrackerDatabase.organizationDao()
    }
}
