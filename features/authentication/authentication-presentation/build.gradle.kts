import dependencies.android.Android
import dependencies.modules.Modules.Common.DESIGN_SYSTEM
import dependencies.projectconfig.ProjectConfig.AUTHENTICATION_PRESENTATION_NAME_SPACE
import dependencies.projectconfig.ProjectConfig.COMPILE_SDK
import dependencies.projectconfig.ProjectConfig.DEBUG
import dependencies.projectconfig.ProjectConfig.JVM_TARGET
import dependencies.projectconfig.ProjectConfig.MIN_SDK
import dependencies.projectconfig.ProjectConfig.PROD
import dependencies.projectconfig.ProjectConfig.RELEASE
import dependencies.projectconfig.ProjectConfig.STAGING
import dependencies.projectconfig.ProjectConfig.VERSION

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    jacoco
}

android {
    compileSdk = COMPILE_SDK
    namespace = AUTHENTICATION_PRESENTATION_NAME_SPACE

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

    buildFeatures {
        compose = true
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

    // Modules
    implementation(project(DESIGN_SYSTEM))
}