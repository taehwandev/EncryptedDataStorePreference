@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}

dependencies {
    implementation(libs.plugin.androidGradlePlugin)
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.ksp)
}