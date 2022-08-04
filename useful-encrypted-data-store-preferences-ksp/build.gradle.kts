import tech.thdev.gradle.dependencies.Dependency
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
    implementation(Dependency.Coroutines.core)
    implementation(Dependency.Ksp.processingApi)
    implementation(Dependency.Ksp.kotlinPoet)

    implementation(projects.usefulEncryptedDataStorePreferencesKspAnnotations)


    Dependency.Ksp.run {
        testImplementation(kspCompileTesting)
        testImplementation(kspCompileTestingKsp)
    }
    Dependency.AndroidTest.run {
        testImplementation(junit5)
        testImplementation(junit5Engine)
    }
}