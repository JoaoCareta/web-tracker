package com.joao.otavio.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WebTrackerDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): WebTrackerDataStore {
    override suspend fun <T> savePreference(key: Preferences.Key<T>, value: T) {
        dataStore.edit { settings ->
            settings[key] = value
        }
    }

    override suspend fun <T> getPreference(key: Preferences.Key<T>, default: T?): T? {
        return dataStore.data.map { preferences ->
            preferences[key] ?: default
        }.firstOrNull()
    }

    override suspend fun <T> removePreference(key: Preferences.Key<T>) {
        dataStore.edit { settings ->
            settings.remove(key)
        }
    }

    override suspend fun clearAllPreference() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
