@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")

    kotlin("android")
}

android {
    namespace = "tech.thdev.samplepreference.repository"

    buildToolsVersion = libs.versions.buildToolsVersion.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk =  libs.versions.minSdk.get().toInt()
        setCompileSdkVersion(libs.versions.targetSdk.get().toInt())

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compilerVersion.get()
    }

    buildTypes {
        sourceSets.getByName("debug") {
            kotlin.srcDir("build/generated/ksp/debug/kotlin")
        }
        sourceSets.getByName("release") {
            kotlin.srcDir("build/generated/ksp/release/kotlin")
        }
    }
}

dependencies {
    implementation(libs.androidx.dataStorePreferences)
    implementation(libs.androidx.securityCrypto)

    // use - current release version
//    ksp(libs.usefulDataStorePreference.ksp)
//    implementation(libs.usefulDataStorePreference.ksp.annotation)
//    implementation(libs.usefulDataStorePreference.security)

    // use - local
    ksp(projects.usefulEncryptedDataStorePreferencesKsp)
    implementation(projects.usefulEncryptedDataStorePreferencesKspAnnotations)
    implementation(projects.usefulEncryptedDataStorePreferencesSecurity)
}