package dependencies.android

object Android {
    const val ANDROID_APPLICATION = "com.android.application"
    const val ANDROID_LIBRARY = "com.android.library"

    object AndroidX {
        const val COMPOSE_COMPILER = "1.5.15"

        private const val COMPOSE_NAVIGATION_VERSION = "2.9.0"
        const val COMPOSE_NAVIGATION = "androidx.navigation:navigation-compose:$COMPOSE_NAVIGATION_VERSION"

        private const val LIFE_CYCLE_VIEW_MODEL_VERSION = "2.9.0"
        const val LIFE_CYCLE_VIEW_MODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:$LIFE_CYCLE_VIEW_MODEL_VERSION"

        private const val DATA_STORE_VERSION = "1.1.7"
        const val DATA_STORE = "androidx.datastore:datastore-preferences:$DATA_STORE_VERSION"
    }
}
