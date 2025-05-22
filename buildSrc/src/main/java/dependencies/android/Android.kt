package dependencies.android

object Android {
    const val ANDROID_APPLICATION = "com.android.application"
    const val ANDROID_LIBRARY = "com.android.library"

    object AndroidX {
        const val COMPOSE_COMPILER = "1.5.15"

        private const val COMPOSE_NAVIGATION_VERSION = "2.9.0"
        const val COMPOSE_NAVIGATION = "androidx.navigation:navigation-compose:$COMPOSE_NAVIGATION_VERSION"
    }
}
