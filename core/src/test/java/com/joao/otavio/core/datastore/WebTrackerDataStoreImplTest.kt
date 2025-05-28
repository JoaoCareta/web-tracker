package com.joao.otavio.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.mutablePreferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WebTrackerDataStoreImplTest {
    private val dataStore: DataStore<Preferences> = mockk()
    private val webTrackerDataStoreImpl = WebTrackerDataStoreImpl(dataStore = dataStore)

    @Before
    fun setup() {
        coJustRun { dataStore.updateData(any()) }
    }

    @Test
    fun `given a dataStore key and value, when dataStore succeed in saving it, then no exception should be thrown`() =
        runTest {
            // Mockk
            coJustRun { dataStore.edit { settings -> settings[KEY] = VALUE } }

            // Test
            webTrackerDataStoreImpl.savePreference(KEY, VALUE)
        }

    @Test
    fun `given a saved preference, when the user tries to get, then it should return it`() =
        runTest {
            // Mockk
            val mutablePreferences = mutablePreferencesOf(KEY to VALUE)
            coJustRun { dataStore.edit { settings -> settings[KEY] = VALUE } }
            coEvery { dataStore.data } returns MutableStateFlow<Preferences>(mutablePreferences)

            // Test
            webTrackerDataStoreImpl.savePreference(KEY, VALUE)
            val result = webTrackerDataStoreImpl.getPreference(KEY)

            // Assert
            assertEquals(result, VALUE)
        }

    @Test
    fun `given a unsaved preference, when the user tries to get, then it should return null`() =
        runTest {
            // Mockk
            val mutablePreferences = mutablePreferencesOf()
            coEvery { dataStore.data } returns MutableStateFlow<Preferences>(mutablePreferences)

            // Test
            val result = webTrackerDataStoreImpl.getPreference(KEY)

            // Assert
            assertNull(result)
        }

    @Test
    fun `given a preference, when the user wants to delete it, then it should be able to do it`() =
        runTest {
            // Mockk
            coJustRun { dataStore.edit { settings -> settings[KEY] = VALUE } }
            coJustRun {
                dataStore.edit { settings ->
                    settings.remove(KEY)
                }
            }

            // Test
            webTrackerDataStoreImpl.savePreference(KEY, VALUE)
            webTrackerDataStoreImpl.removePreference(KEY)
        }

    @Test
    fun `given a dataStore, when the user wants to clear it, then it should be able to do it`() =
        runTest {
            // Mockk
            coJustRun { dataStore.edit { settings -> settings[KEY] = VALUE } }
            coJustRun {
                dataStore.edit { settings ->
                    settings.remove(KEY)
                }
            }

            // Test
            webTrackerDataStoreImpl.savePreference(KEY, VALUE)
            webTrackerDataStoreImpl.clearAllPreference()
        }

    companion object {
        val KEY: Preferences.Key<String> = stringPreferencesKey("key")
        const val VALUE = "value"
    }
}
