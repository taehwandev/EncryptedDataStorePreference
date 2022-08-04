import tech.thdev.gradle.dependencies.Dependency
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

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(Dependency.Coroutines.core)
    implementation(Dependency.AndroidX.datastorePreferencesCore)
    implementation(Dependency.AndroidX.securityCrypto)

    Dependency.AndroidTest.run {
        testImplementation(androidxCore)
        testImplementation(androidxRunner)
        testImplementation(androidxJunit)
        testImplementation(robolectric)
        testImplementation(mockitoKotlin)
        testImplementation(junit5)
        testImplementation(junit5Engine)
        testRuntimeOnly(junit5Vintage)
    }
    testImplementation(Dependency.Coroutines.test)
    testImplementation(Dependency.Coroutines.turbine)
    testImplementation(Dependency.AndroidX.datastorePreferences)
}