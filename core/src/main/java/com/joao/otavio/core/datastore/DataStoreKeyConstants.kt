package com.joao.otavio.core.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

class DataStoreKeyConstants {
    object WebTrackerAuthentication {
        val FIREBASE_ORG_ID = stringPreferencesKey("firebase_org_id")
        val FIREBASE_ORG_NAME = stringPreferencesKey("firebase_org_name")
    }
}
