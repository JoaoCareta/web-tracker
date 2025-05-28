package com.joao.otavio.core.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

class DataStoreKeyConstants {
    object WebTrackerAuthentication {
        val FIREBASE_USER_ID = stringPreferencesKey("firebase_user_id")
    }
}
