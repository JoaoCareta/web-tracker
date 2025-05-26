import dependencies.android.Android.AndroidX.COMPOSE_NAVIGATION
import dependencies.android.Android.AndroidX.DATA_STORE
import dependencies.firebase.Firebase.FIREBASE_ANALYTICS
import dependencies.firebase.Firebase.FIREBASE_CRASHLYTICS
import dependencies.hilt.DaggerHilt.DAGGER_HILT_ANDROID
import dependencies.hilt.DaggerHilt.DAGGER_HILT_COMPILER
import dependencies.modules.Modules.Common.AUTHENTICATION_PRESENTATION
import dependencies.modules.Modules.Common.CORE
import dependencies.modules.Modules.Common.DESIGN_SYSTEM
import dependencies.projectconfig.ProjectConfig.APP_ID
import dependencies.projectconfig.ProjectConfig.APP_NAME
import dependencies.projectconfig.ProjectConfig.COMPILE_SDK
import dependencies.projectconfig.ProjectConfig.DEBUG
import dependencies.projectconfig.ProjectConfig.DEBUG_SUFFIX
import dependencies.projectconfig.ProjectConfig.JVM_TARGET
import dependencies.projectconfig.ProjectConfig.KEY_ALIAS
import dependencies.projectconfig.ProjectConfig.MIN_SDK
import dependencies.projectconfig.ProjectConfig.NAME_SPACE
import dependencies.projectconfig.ProjectConfig.PROD
import dependencies.projectconfig.ProjectConfig.PROD_SUFFIX
import dependencies.projectconfig.ProjectConfig.RELEASE
import dependencies.projectconfig.ProjectConfig.RELEASE_NOTES_FILE
import dependencies.projectconfig.ProjectConfig.STAGING
import dependencies.projectconfig.ProjectConfig.STAGING_SUFFIX
import dependencies.projectconfig.ProjectConfig.STORE_PASSWORD
import dependencies.projectconfig.ProjectConfig.TARGET_SDK
import dependencies.projectconfig.ProjectConfig.TESTERS_GROUP_NAME
import dependencies.projectconfig.ProjectConfig.VERSION
import dependencies.projectconfig.ProjectConfig.KEY_PASSWORD
import dependencies.projectconfig.ProjectConfig.STORE_FILE
import dependencies.scripts.Scripts.GIT_VERSION_PATH

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.firebase.plugin)
    alias(libs.plugins.firebase.app.distribuiton.plugin)
    alias(libs.plugins.crashlytics.plugin)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt)
}

apply("$rootDir/$GIT_VERSION_PATH")

val appName = APP_NAME

android {
    namespace = NAME_SPACE
    compileSdk = COMPILE_SDK

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    signingConfigs {
        create(RELEASE) {
            keyAlias = System.getenv(KEY_ALIAS) ?: ""
            keyPassword = System.getenv(KEY_PASSWORD) ?: ""
            storeFile = file("keystore/release.keystore")
            storePassword = System.getenv(STORE_PASSWORD) ?: ""
        }
    }

    defaultConfig {
        applicationId = APP_ID
        minSdk = MIN_SDK
        targetSdk = TARGET_SDK
        versionCode = "${extra["gitVersionCode"] ?: 3333}".toInt()
        versionName = "${extra["gitVersionName"] ?: "1.1.0"}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName(DEBUG) {
            applicationIdSuffix = DEBUG_SUFFIX
            isDebuggable = true
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName(DEBUG)
            isDefault = true
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }

        getByName(RELEASE) {
            /**
             * Enables code shrinking, obfuscation, and optimization for only
             * your project's release build type.
             */
            isMinifyEnabled = true
            isDebuggable = false

            /**
             * Enables resource shrinking, which is performed by the Android Gradle Plugin
             */
            isShrinkResources = true

            /**
             * Includes the default ProGuard rules files that are packaged with
             * the Android Gradle plugin.
             */
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName(RELEASE)
            enableUnitTestCoverage = false
            enableAndroidTestCoverage = false

            firebaseAppDistribution {
                groups = TESTERS_GROUP_NAME
                releaseNotesFile = RELEASE_NOTES_FILE
            }
        }
    }

    flavorDimensions += listOf(VERSION)

    productFlavors {
        create(STAGING) {
            /**
             * manifestPlaceholders += mapOf(
             *                 "appIcon" to "@mipmap/ic_launcher_qa",
             *                 "appRoundIcon" to "@mipmap/ic_launcher_qa_round",
             *                 "appAuthRedirectScheme" to "com.cropwise.scout.staging"
             *             )
             */

            dimension = VERSION
            applicationIdSuffix = STAGING_SUFFIX
            resValue("string", "app_name", "$appName QA")
            isDefault = true
        }

        create(PROD) {
            /**
             * manifestPlaceholders += mapOf(
             *                 "appIcon" to "@mipmap/ic_launcher",
             *                 "appRoundIcon" to "@mipmap/ic_launcher_round",
             *                 "appAuthRedirectScheme" to "com.cropwise.scout"
             *             )
             */
            dimension = VERSION
            applicationIdSuffix = PROD_SUFFIX
            resValue("string", "app_name", appName)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JVM_TARGET
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // AndroidX - Core e Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation (DATA_STORE)

    // AndroidX - Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // AndroidX - Testes
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // AndroidX - Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Navegação com Compose
    implementation(COMPOSE_NAVIGATION)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(FIREBASE_CRASHLYTICS)
    implementation(FIREBASE_ANALYTICS)

    // Hilt
    kapt(DAGGER_HILT_COMPILER)
    implementation(DAGGER_HILT_ANDROID)

    // Modules
    implementation(project(DESIGN_SYSTEM))
    implementation(project(AUTHENTICATION_PRESENTATION))
    implementation(project(CORE))
}