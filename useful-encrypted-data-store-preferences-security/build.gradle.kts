@file:Suppress("UnstableApiUsage")

import tech.thdev.gradle.dependencies.Publish

plugins {
    id("com.android.library")
    kotlin("android")
    id("lib-publish-android")
}

ext["libraryName"] = "useful-encrypted-data-store-preferences-security"
ext["libraryVersion"] = libs.versions.libraryVersion.get()
ext["description"] = Publish.description
ext["url"] = Publish.publishUrl

android {
    namespace = "tech.thdev.useful.encrypted.data.store.preferences.security"

    buildToolsVersion = libs.versions.buildToolsVersion.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // AGP 8.0
    publishing {
        multipleVariants("release") {
            allVariants()
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(libs.coroutines.core)
    implementation(libs.androidx.dataStorePreferencesCore)
    implementation(libs.androidx.securityCrypto)

    testImplementation(libs.test.androidx.core)
    testImplementation(libs.test.androidx.runner)
    testImplementation(libs.test.androidx.junit)
    testImplementation(libs.test.robolectric)
    testImplementation(libs.test.mockito.kotlin)
    testImplementation(libs.test.junit5)
    testImplementation(libs.test.junit5.engine)
    testRuntimeOnly(libs.test.junit5.vintage)
    testImplementation(libs.test.coroutines)
    testImplementation(libs.test.coroutines.turbine)

    testImplementation(libs.androidx.dataStorePreferences)
}