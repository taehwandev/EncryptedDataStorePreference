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
}

dependencies {
    implementation(Dependency.AndroidX.datastorePreferences)
    implementation(Dependency.AndroidX.securityCrypto)

    ksp(project(":useful-encrypted-data-store-preferences-ksp"))
    implementation(project(":useful-encrypted-data-store-preferences-security"))
}