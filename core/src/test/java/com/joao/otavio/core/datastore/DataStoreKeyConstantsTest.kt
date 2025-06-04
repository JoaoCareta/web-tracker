package com.joao.otavio.core.datastore

import androidx.datastore.preferences.core.stringPreferencesKey
import org.junit.Assert.assertEquals
import org.junit.Test

class DataStoreKeyConstantsTest {

    @Test
    fun `verify FIREBASE_ORG_ID key name`() {
        // Mockk
        val expectedKey = stringPreferencesKey("firebase_org_id")

        // Run Test
        val result = DataStoreKeyConstants.WebTrackerAuthentication.FIREBASE_ORG_ID

        // Assert
        assertEquals(expectedKey, result)
    }

    @Test
    fun `verify FIREBASE_ORG_ID key name string value`() {
        // Mockk
        val expectedKeyName = "firebase_org_id"

        // Run Test
        val result = DataStoreKeyConstants.WebTrackerAuthentication.FIREBASE_ORG_ID.name

        // Assert
        assertEquals(expectedKeyName, result)
    }
}
