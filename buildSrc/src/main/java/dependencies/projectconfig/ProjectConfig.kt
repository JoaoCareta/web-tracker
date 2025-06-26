package dependencies.projectconfig

object ProjectConfig {
    /**
     * Android
     */
    const val APP_NAME = "Web Tracker"
    const val NAME_SPACE = "com.joao.otavio.webtracker"

    const val DESIGN_SYSTEM_NAME_SPACE = "com.joao.otavio.webtracker.common.desygn.system"
    const val UTILS_NAME_SPACE = "com.joao.otavio.webtracker.common.utils"
    const val CORE_NAME_SPACE = "com.joao.otavio.webtracker.core"

    const val AUTHENTICATION_PRESENTATION_NAME_SPACE = "com.joao.otavio.webtracker.features.authentication.authentication.presentation"
    const val AUTHENTICATION_DOMAIN_NAME_SPACE = "com.joao.otavio.webtracker.features.authentication.authentication.domain"
    const val AUTHENTICATION_DATA_NAME_SPACE = "com.joao.otavio.webtracker.features.authentication.authentication.data"

    const val IDENTIFICATION_PRESENTATION_NAME_SPACE = "com.joao.otavio.webtracker.features.identification.identification.presentation"

    const val MAP_PRESENTATION_NAME_SPACE = "com.joao.otavio.webtracker.features.map.map.presentation"

    const val APP_ID = "com.joao.otavio.webtracker"
    const val COMPILE_SDK = 35
    const val MIN_SDK = 24
    const val TARGET_SDK = 35
    const val GIT_VERSION_CODE = "gitVersionCode"
    const val GIT_VERSION_NAME = "gitVersionName"

    /**
     * SIGNING CONFIG
     */
    const val KEY_ALIAS = "KEY_ALIAS"
    const val KEY_PASSWORD = "KEY_PASSWORD"
    const val STORE_PASSWORD = "STORE_PASSWORD"
    const val KEYSTORE_PATH = "KEYSTORE_PATH"
    const val LOCAL_KEYSTORE_PATH = "keystore"

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

    /**
     * MAPBOX CONFIG
     */
    const val MAPBOX_ACCESS_TOKEN = "MAPBOX_ACCESS_TOKEN"

    /**
     * PROPERTIES CONFIG
     */
    const val KEYSTORE_PROPERTIES = "keystore.properties"
    const val LOCAL_PROPERTIES = "local.properties"
}
