plugins {
    kotlin("jvm")
    id("lib-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

ext["libraryName"] = "useful-encrypted-data-store-preferences-ksp-annotations"
ext["libraryVersion"] = "1.0.0-alpha01"
ext["description"] = "Android Encrypted DataStorePreferences"
ext["url"] = "https://thdev.tech/EncryptedDataStorePreference/"