package dependencies.hilt

object DaggerHilt {
    private const val DAGGER_HILT_VERSION = "2.51.1"
    private const val DAGGER_HILT_VIEWMODEL_VERSION = "1.2.0"

    const val DAGGER_HILT_ANDROID = "com.google.dagger:hilt-android:$DAGGER_HILT_VERSION"
    const val DAGGER_HILT_VIEWMODEL = "androidx.hilt:hilt-navigation-compose:$DAGGER_HILT_VIEWMODEL_VERSION"
    const val DAGGER_HILT_COMPILER = "com.google.dagger:hilt-android-compiler:$DAGGER_HILT_VERSION"
}
