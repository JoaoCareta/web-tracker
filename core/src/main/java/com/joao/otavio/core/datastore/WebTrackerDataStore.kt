package com.joao.otavio.core.datastore

import androidx.datastore.preferences.core.Preferences

interface WebTrackerDataStore {
    suspend fun <T> savePreference(key: Preferences.Key<T>, value: T)

    suspend fun <T> getPreference(key: Preferences.Key<T>, default: T? = null): T?

    suspend fun <T> removePreference(key: Preferences.Key<T>)

    suspend fun clearAllPreference()
}
