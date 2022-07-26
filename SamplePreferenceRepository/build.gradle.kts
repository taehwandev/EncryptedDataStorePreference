import tech.thdev.gradle.dependencies.Dependency

plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")

    kotlin("android")
}

android {
    namespace = "tech.thdev.samplepreference.repository"

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

    composeOptions {
        kotlinCompilerExtensionVersion = Dependency.Compose.composeCompilerVersion
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
    implementation(Dependency.AndroidX.datastorePreferences)
    implementation(Dependency.AndroidX.securityCrypto)

    // use - current release version
//    ksp(Dependency.UsefulDataStorePreference.ksp)
//    implementation(Dependency.UsefulDataStorePreference.kspAnnotation)
//    implementation(Dependency.UsefulDataStorePreference.security)

    // use - local
    ksp(projects.usefulEncryptedDataStorePreferencesKsp)
    implementation(projects.usefulEncryptedDataStorePreferencesKspAnnotations)
    implementation(projects.usefulEncryptedDataStorePreferencesSecurity)
}