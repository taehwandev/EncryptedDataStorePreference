@file:Suppress("UnstableApiUsage")

import tech.thdev.gradle.dependencies.Publish

plugins {
    id("com.android.library")
    kotlin("android")
    id("lib-publish-android")
}

ext["libraryName"] = "useful-encrypted-data-store-preferences-security"
ext["libraryVersion"] = Publish.libraryVersion
ext["description"] = Publish.description
ext["url"] = Publish.publishUrl

android {
    namespace = "tech.thdev.useful.encrypted.data.store.preferences.security"

    buildToolsVersion = libs.versions.buildToolsVersion.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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

    libs.test.run {
        testImplementation(androidx.core)
        testImplementation(androidx.runner)
        testImplementation(androidx.junit)
        testImplementation(robolectric)
        testImplementation(mockito.kotlin)
        testImplementation(junit5)
        testImplementation(junit5.engine)
        testRuntimeOnly(junit5.vintage)
        testImplementation(coroutines)
        testImplementation(coroutines.turbine)
    }
    testImplementation(libs.androidx.dataStorePreferences)
}