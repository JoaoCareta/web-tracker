pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Mapbox Maven repository
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
        }

    }
}

rootProject.name = "Web Tracker"
include(":app")
include(":common")
include(":common:design-system")
include(":features")
include(":features:authentication")
include(":features:authentication:authentication-presentation")
include(":core")
include(":features:authentication:authentication-domain")
include(":features:authentication:authentication-data")
include(":common:utils")
include(":features:identification")
include(":features:identification:identification-presentation")
include(":features:map")
include(":features:map:map-presentation")
