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
