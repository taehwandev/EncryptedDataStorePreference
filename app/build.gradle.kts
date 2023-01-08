@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "tech.thdev.encrypteddatastorepreference"

    buildToolsVersion = libs.versions.buildToolsVersion.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk =  libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = "${libs.versions.major.get()}.${libs.versions.minor.get()}.${libs.versions.hotfix.get()}"

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
        kotlinCompilerExtensionVersion = libs.versions.compose.compilerVersion.get()
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
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.lifecycleCommonJava8)
    implementation(libs.androidx.dataStorePreferences)
    implementation(libs.androidx.securityCrypto)
    implementation(libs.compose.activity)
    implementation(libs.compose.ui)
    implementation(libs.compose.uiToolingPreview)
    implementation(libs.compose.material)

    debugImplementation(libs.compose.uiTooling)
    debugImplementation(libs.compose.ui)

    implementation(projects.samplePreferenceRepository)

    // use - local
    implementation(projects.usefulEncryptedDataStorePreferencesSecurity)
}