import dependencies.android.Android
import dependencies.android.Android.AndroidX.LIFE_CYCLE_VIEW_MODEL
import dependencies.firebase.Firebase.FIREBASE_AUTHENTICATION
import dependencies.hilt.DaggerHilt.DAGGER_HILT_ANDROID
import dependencies.hilt.DaggerHilt.DAGGER_HILT_COMPILER
import dependencies.hilt.DaggerHilt.DAGGER_HILT_VIEWMODEL
import dependencies.modules.Modules.Common.AUTHENTICATION_PRESENTATION
import dependencies.modules.Modules.Common.CORE
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
import dependencies.testing.Testing.COROUTINE_TEST
import dependencies.testing.Testing.MOCKK
import dependencies.testing.Testing.MOCKK_ANDROID

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt)
    jacoco
}

sonarqube {
    properties {
        property ("sonar.sources", "src/main/java")
        property ("sonar.tests", "src/test/java")
        property ("sonar.coverage.jacoco.xmlReportPaths", "${buildDir}/reports/jacoco/jacocoTestReport-staging-unified/jacocoTestReport-staging-unified.xml")
    }
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
    // AndroidX - Core e Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(LIFE_CYCLE_VIEW_MODEL)

    // AndroidX - Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Coroutines - Test
    implementation(COROUTINE_TEST)

    // Firebase
    implementation(FIREBASE_AUTHENTICATION)

    // Dagger Hilt
    kapt(DAGGER_HILT_COMPILER)
    implementation(DAGGER_HILT_ANDROID)
    implementation(DAGGER_HILT_VIEWMODEL)

    // Modules
    implementation(project(AUTHENTICATION_PRESENTATION))
    implementation(project(DESIGN_SYSTEM))
    implementation(project(CORE))

    // AndroidX - Testes
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    testImplementation(MOCKK)
    androidTestImplementation(MOCKK_ANDROID)
    // Coroutines - Test
    testImplementation(COROUTINE_TEST)

    // AndroidX - Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
