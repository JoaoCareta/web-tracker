package com.joao.otavio.core.di

import android.content.Context
import androidx.room.Room
import com.joao.otavio.core.databse.WebTrackerDatabase
import com.joao.otavio.core.databse.WebTrackerDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideWebTrackerDatabase(
        @ApplicationContext context: Context
    ): WebTrackerDatabase {
        return Room.databaseBuilder(
            context,
            WebTrackerDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}
