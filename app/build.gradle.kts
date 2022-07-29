import tech.thdev.gradle.dependencies.Dependency

plugins {
    id("com.android.application")
    id("com.google.devtools.ksp")

    kotlin("android")
}

android {
    namespace = "tech.thdev.encrypteddatastorepreference"

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

        getByName("release") {
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Dependency.Compose.composeCompilerVersion
    }

    packagingOptions {
        resources.excludes.addAll(
            listOf(
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
            )
        )
    }
}

dependencies {
    implementation(Dependency.AndroidX.coreKtx)
    implementation(Dependency.AndroidX.lifecycleRuntimeKtx)
    implementation(Dependency.AndroidX.datastorePreferences)
    implementation(Dependency.AndroidX.securityCrypto)
    implementation(Dependency.Compose.activity)
    implementation(Dependency.Compose.ui)
    implementation(Dependency.Compose.uiToolingPreview)
    implementation(Dependency.Compose.material)

    debugImplementation(Dependency.Compose.uiTooling)
    debugImplementation(Dependency.Compose.uiTooling)

    implementation(project(":SamplePreferenceRepository"))
    implementation(project(":useful-encrypted-data-store-preferences-security"))
}