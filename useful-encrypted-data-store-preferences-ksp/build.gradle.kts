import tech.thdev.gradle.dependencies.Publish

plugins {
    kotlin("jvm")
    id("lib-publish")
}

ext["libraryName"] = "useful-encrypted-data-store-preferences-ksp"
ext["libraryVersion"] = Publish.libraryVersion
ext["description"] = Publish.description
ext["url"] = Publish.publishUrl

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<Test>() {
    useJUnitPlatform()
}

dependencies {
    implementation(libs.coroutines.core)
    implementation(libs.ksp)
    implementation(libs.ksp.kotlinPoet)

    implementation(projects.usefulEncryptedDataStorePreferencesKspAnnotations)

    libs.test.run {
        testImplementation(kotlinCompilTesting)
        testImplementation(kotlinCompilTestingKSP)
        testImplementation(junit5)
        testImplementation(junit5.engine)
    }
}