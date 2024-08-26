import tech.thdev.gradle.dependencies.Publish

plugins {
    kotlin("jvm")
    id("lib-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

ext["libraryName"] = "useful-encrypted-data-store-preferences-ksp-annotations"
ext["libraryVersion"] = libs.versions.libraryVersion.get()
ext["description"] = Publish.description
ext["url"] = Publish.publishUrl