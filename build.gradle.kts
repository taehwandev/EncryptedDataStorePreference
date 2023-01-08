buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.plugin.androidGradlePlugin)
        classpath(libs.plugin.kotlin)
        classpath(libs.plugin.ksp)
    }
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"

        kotlinOptions.allWarningsAsErrors = false

        kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
        kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.Experimental"
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}