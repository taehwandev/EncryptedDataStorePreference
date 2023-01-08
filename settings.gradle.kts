@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "EncryptedDataStorePreference"
// Sample
include(
    ":app",
    ":SamplePreferenceRepository",
)

// library
include(
    ":useful-encrypted-data-store-preferences-security",
    ":useful-encrypted-data-store-preferences-ksp",
    ":useful-encrypted-data-store-preferences-ksp-annotations",
)