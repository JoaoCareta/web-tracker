plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

apply("$rootDir/config/gitversion/script-git-version.gradle")

val appName = "Web Tracker"

android {
    namespace = "com.joao.otavio.webtracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.joao.otavio.webtracker"
        minSdk = 24
        targetSdk = 35
        versionCode = "${extra["gitVersionCode"] ?: 3333}".toInt()
        versionName = "${extra["gitVersionName"] ?: "1.1.0"}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            isMinifyEnabled = false
            // signingConfig = signingConfigs.getByName("debug")
            isDefault = true
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }

        getByName("release") {
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

            // signingConfig = signingConfigs.getByName("release")
            enableUnitTestCoverage = false
            enableAndroidTestCoverage = false
        }
    }

    flavorDimensions += listOf("version")

    productFlavors {
        create("staging") {
            /**
             * manifestPlaceholders += mapOf(
             *                 "appIcon" to "@mipmap/ic_launcher_qa",
             *                 "appRoundIcon" to "@mipmap/ic_launcher_qa_round",
             *                 "appAuthRedirectScheme" to "com.cropwise.scout.staging"
             *             )
             */

            dimension = "version"
            applicationIdSuffix = ".hom"
            resValue("string", "app_name", "$appName QA")
            isDefault = true
        }

        create("prod") {
            /**
             * manifestPlaceholders += mapOf(
             *                 "appIcon" to "@mipmap/ic_launcher",
             *                 "appRoundIcon" to "@mipmap/ic_launcher_round",
             *                 "appAuthRedirectScheme" to "com.cropwise.scout"
             *             )
             */
            dimension = "version"
            resValue("string", "app_name", appName)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}