package com.joao.otavio.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

object DataStoreFactory {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "SETTINGS_DATA_STORE_NAME")

    fun create(context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}
