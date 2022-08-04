package tech.thdev.gradle.dependencies

object Dependency {

    object Base {
        const val buildToolsVersion = "31.0.0"
        const val compileVersion = 33
        const val targetSdk = 33
        const val minSdk = 24
    }

    object Kotlin {
        // https://github.com/JetBrains/kotlin
        const val version: String = "1.7.10"

        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
    }

    object Coroutines {
        // https://github.com/Kotlin/kotlinx.coroutines
        private const val version: String = "1.6.4"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"

        // https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"

        // https://github.com/cashapp/turbine
        const val turbine = "app.cash.turbine:turbine:0.7.0"
    }

    object AndroidX {
        // https://developer.android.com/jetpack/androidx/releases/core
        const val coreKtx = "androidx.core:core-ktx:1.8.0"

        const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.5.1"

        // https://developer.android.com/jetpack/androidx/releases/appcompat
        const val appCompat = "androidx.appcompat:appcompat:1.3.1"

        // https://developer.android.com/jetpack/androidx/releases/activity
        const val activity = "androidx.activity:activity-ktx:1.3.1"

        // https://developer.android.com/jetpack/androidx/releases/constraintlayout
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.1"

        // https://developer.android.com/jetpack/androidx/releases/vectordrawable
        const val vectorDrawable = "androidx.vectordrawable:vectordrawable:1.1.0"

        private const val dataStoreVersion = "1.0.0"
        const val datastorePreferences = "androidx.datastore:datastore-preferences:$dataStoreVersion"
        const val datastorePreferencesCore = "androidx.datastore:datastore-preferences-core:$dataStoreVersion"

        const val securityCrypto = "androidx.security:security-crypto:1.1.0-alpha03"
    }

    object Compose {
        // https://developer.android.com/jetpack/androidx/releases/compose
        private const val version = "1.3.0-alpha01"
        const val composeCompilerVersion = "1.3.0-beta01"

        const val ui = "androidx.compose.ui:ui:$version"
        const val uiTooling = "androidx.compose.ui:ui-tooling:$version"
        const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:$version"
        const val material = "androidx.compose.material:material:$version"

        const val activity = "androidx.activity:activity-compose:1.5.1"
    }

    object Ksp {
        // https://github.com/square/kotlinpoet
        const val kotlinPoet = "com.squareup:kotlinpoet:1.12.0"

        // https://github.com/google/ksp
        const val kspVersion = "1.7.10-1.0.6"
        const val processingApi = "com.google.devtools.ksp:symbol-processing-api:$kspVersion"

        // https://github.com/tschuchortdev/kotlin-compile-testing
        private const val kotlinCompileTestVersion = "1.4.9"
        const val kspCompileTesting = "com.github.tschuchortdev:kotlin-compile-testing:$kotlinCompileTestVersion"
        const val kspCompileTestingKsp = "com.github.tschuchortdev:kotlin-compile-testing-ksp:$kotlinCompileTestVersion"
    }

    object UsefulDataStorePreference {
        private const val version = "1.0.0-alpha01"

        const val ksp = "tech.thdev:useful-encrypted-data-store-preferences-ksp:$version"
        const val kspAnnotation = "tech.thdev:useful-encrypted-data-store-preferences-ksp-annotations:$version"
        const val security = "tech.thdev:useful-encrypted-data-store-preferences-security:$version"
    }

    object AndroidTest {
        // https://developer.android.com/jetpack/androidx/releases/test
        const val androidxCore = "androidx.test:core:1.5.0-alpha01"
        const val androidxRunner = "androidx.test:runner:1.5.0-alpha04"
        const val androidxJunit = "androidx.test.ext:junit:1.1.4-alpha07"

        // https://github.com/mockito/mockito-kotlin
        const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:4.0.0"

        // https://github.com/mannodermaus/android-junit5
        private const val junit5Version = "5.8.2"

        // (Required) Writing and executing Unit Tests on the JUnit Platform
        const val junit5 = "org.junit.jupiter:junit-jupiter-api:$junit5Version"
        const val junit5Engine = "org.junit.jupiter:junit-jupiter-engine:$junit5Version"

        // jUnit4 호환 모듈
        const val junit5Vintage = "org.junit.vintage:junit-vintage-engine:$junit5Version"

        // https://github.com/robolectric/robolectric
        private const val robolectricVersion = "4.8.1"
        const val robolectric = "org.robolectric:robolectric:$robolectricVersion"
    }
}