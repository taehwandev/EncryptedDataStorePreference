import tech.thdev.gradle.dependencies.Dependency

plugins {
    kotlin("jvm")
    id("lib-publish")
}

ext["libraryName"] = "useful-encrypted-data-store-preferences-ksp"
ext["libraryVersion"] = "1.0.0-alpha01"
ext["description"] = "Android Encrypted DataStorePreferences"
ext["url"] = "https://thdev.tech/EncryptedDataStorePreference/"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(Dependency.Ksp.processingApi)
    implementation(Dependency.Ksp.kotlinPoet)

    implementation(project(":useful-encrypted-data-store-preferences-ksp-annotations"))
}