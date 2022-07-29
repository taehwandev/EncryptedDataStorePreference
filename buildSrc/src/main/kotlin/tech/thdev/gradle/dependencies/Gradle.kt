package tech.thdev.gradle.dependencies

object Gradle {
    const val android = "com.android.tools.build:gradle:7.2.1"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Dependency.Kotlin.version}"
    const val ksp = "com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${Dependency.Ksp.kspVersion}"
}