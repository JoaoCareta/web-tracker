package dependencies.projectconfig

object ProjectConfig {
    /**
     * Android
     */
    const val APP_NAME = "Web Tracker"
    const val NAME_SPACE = "com.joao.otavio.webtracker"
    const val DESIGN_SYSTEM_NAME_SPACE = "com.joao.otavio.webtracker.common.desygn.system"
    const val AUTHENTICATION_PRESENTATION_NAME_SPACE = "com.joao.otavio.webtracker.features.authentication.authentication.presentation"
    const val AUTHENTICATION_DOMAIN_NAME_SPACE = "com.joao.otavio.webtracker.features.authentication.authentication.domain"
    const val APP_ID = "com.joao.otavio.webtracker"
    const val COMPILE_SDK = 35
    const val MIN_SDK = 24
    const val TARGET_SDK = 35

    /**
     * SIGNING CONFIG
     */
    const val KEY_ALIAS = "web-tracker"
    const val KEY_PASSWORD = "agoravai"
    const val STORE_FILE = "../keystore"
    const val STORE_PASSWORD = "agoravai"

    /**
     * Build Types
     */
    const val DEBUG = "debug"
    const val DEBUG_SUFFIX = ".debug"
    const val RELEASE = "release"

    /**
     * Firebase
     */

    const val TESTERS_GROUP_NAME = "testers"
    const val RELEASE_NOTES_FILE = "release_notes.txt"

    /**
     * Product Flavors
     */
    const val VERSION = "version"
    const val STAGING = "staging"
    const val STAGING_SUFFIX = ".hom"
    const val PROD = "prod"
    const val PROD_SUFFIX = ".prod"


    /**
     * Kotlin Options
     */
    const val JVM_TARGET = "11"
}
