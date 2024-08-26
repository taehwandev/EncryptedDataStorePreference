import tech.thdev.gradle.dependencies.Publish

plugins {
    kotlin("jvm")
    id("lib-publish")
}

ext["libraryName"] = "useful-encrypted-data-store-preferences-ksp"
ext["libraryVersion"] = libs.versions.libraryVersion.get()
ext["description"] = Publish.description
ext["url"] = Publish.publishUrl

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test>() {
    useJUnitPlatform()
}

dependencies {
    implementation(libs.coroutines.core)
    implementation(libs.ksp)
    implementation(libs.ksp.kotlinPoet)

    implementation(projects.usefulEncryptedDataStorePreferencesKspAnnotations)

    testImplementation(libs.test.kotlinCompilTesting)
    testImplementation(libs.test.kotlinCompilTestingKSP)
    testImplementation(libs.test.junit5)
    testImplementation(libs.test.junit5.engine)
}