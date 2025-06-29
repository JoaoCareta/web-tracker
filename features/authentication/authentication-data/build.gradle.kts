import dependencies.android.Android
import dependencies.android.Android.AndroidX.DATA_STORE
import dependencies.gson.Gson.GSON
import dependencies.hilt.DaggerHilt.DAGGER_HILT_ANDROID
import dependencies.hilt.DaggerHilt.DAGGER_HILT_COMPILER
import dependencies.modules.Modules.Common.CORE
import dependencies.modules.Modules.Common.UTILS
import dependencies.projectconfig.ProjectConfig.AUTHENTICATION_DATA_NAME_SPACE
import dependencies.projectconfig.ProjectConfig.COMPILE_SDK
import dependencies.projectconfig.ProjectConfig.CORE_NAME_SPACE
import dependencies.projectconfig.ProjectConfig.DEBUG
import dependencies.projectconfig.ProjectConfig.JVM_TARGET
import dependencies.projectconfig.ProjectConfig.MIN_SDK
import dependencies.projectconfig.ProjectConfig.PROD
import dependencies.projectconfig.ProjectConfig.RELEASE
import dependencies.projectconfig.ProjectConfig.STAGING
import dependencies.projectconfig.ProjectConfig.VERSION
import dependencies.room.Room.ROOM_COMPILER
import dependencies.room.Room.ROOM_KTX
import dependencies.room.Room.ROOM_RUN_TIME
import dependencies.testing.Testing.COROUTINE_TEST
import dependencies.testing.Testing.MOCKK
import dependencies.testing.Testing.MOCKK_ANDROID

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    jacoco
}

android {
    compileSdk = COMPILE_SDK
    namespace = AUTHENTICATION_DATA_NAME_SPACE

    defaultConfig {
        minSdk = MIN_SDK
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName(DEBUG) {
            isMinifyEnabled = false
            isDefault = true
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }

        getByName(RELEASE) {
            enableUnitTestCoverage = false
            enableAndroidTestCoverage = false
        }
    }

    flavorDimensions += listOf(VERSION)

    productFlavors {
        create(STAGING) {
            dimension = VERSION
            isDefault = true
        }

        create(PROD) {
            dimension = VERSION
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JVM_TARGET
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Android.AndroidX.COMPOSE_COMPILER
    }

    packaging {
        resources.excludes.addAll(
            listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md"
            )
        )
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    // AndroidX - Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.graphics)

    // Dagger Hilt
    kapt(DAGGER_HILT_COMPILER)
    implementation(DAGGER_HILT_ANDROID)

    // Room
    kapt(ROOM_COMPILER)
    implementation(ROOM_RUN_TIME)
    implementation(ROOM_KTX)

    // Gson
    implementation(GSON)

    // Modules
    implementation(project(UTILS))

    // AndroidX - Testes
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    testImplementation(MOCKK)
    androidTestImplementation(MOCKK_ANDROID)

    // AndroidX - Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
