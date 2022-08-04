import tech.thdev.gradle.dependencies.Publish

plugins {
    kotlin("jvm")
    id("lib-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

ext["libraryName"] = "useful-encrypted-data-store-preferences-ksp-annotations"
ext["libraryVersion"] = Publish.libraryVersion
ext["description"] = Publish.description
ext["url"] = Publish.publishUrl