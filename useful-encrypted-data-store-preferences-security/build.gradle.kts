import tech.thdev.gradle.dependencies.Dependency

plugins {
    id("com.android.library")
    kotlin("android")
    id("lib-publish-android")
}

ext["libraryName"] = "useful-encrypted-data-store-preferences-security"
ext["libraryVersion"] = "1.0.0-alpha01"
ext["description"] = "Android Encrypted DataStorePreferences"
ext["url"] = "https://thdev.tech/EncryptedDataStorePreference/"

android {
    namespace = "tech.thdev.useful.encrypted.data.store.preferences.security"

    compileSdk = Dependency.Base.compileVersion
    buildToolsVersion = Dependency.Base.buildToolsVersion

    defaultConfig {
        minSdk = Dependency.Base.minSdk
        targetSdk = Dependency.Base.targetSdk
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(Dependency.Coroutines.core)
    implementation(Dependency.AndroidX.datastorePreferencesCore)
    implementation(Dependency.AndroidX.securityCrypto)
}