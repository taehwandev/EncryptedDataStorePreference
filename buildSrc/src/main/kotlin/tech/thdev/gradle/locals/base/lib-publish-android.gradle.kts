import com.android.build.gradle.api.AndroidSourceSet
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.library")
    id("maven-publish")
    id("signing")
}

// Stub secrets to let the project sync and build without the publication values set up
ext["signing.keyId"] = ""
ext["signing.password"] = ""
ext["signing.key"] = ""
ext["ossrhUsername"] = ""
ext["ossrhPassword"] = ""

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

val androidSourceJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName<AndroidSourceSet>("main").java.srcDirs)
}

fun getExtraString(name: String) = ext[name]?.toString()

fun groupId(): String = "tech.thdev"

afterEvaluate {
    // Grabbing secrets from local.properties file or from environment variables, which could be used on CI
    val secretPropsFile = project.rootProject.file("local.properties")
    if (secretPropsFile.exists()) {
        secretPropsFile.reader().use {
            Properties().apply {
                load(it)
            }
        }.onEach { (name, value) ->
            ext[name.toString()] = value
        }
    } else {
        // Use system environment variables
        ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
        ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
        ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
        ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
        ext["signing.key"] = System.getenv("SIGNING_KEY")
    }

    // Set up Sonatype repository
    publishing {
        val artifactName = getExtraString("libraryName") ?: name
        val libraryVersion = getExtraString("libraryVersion") ?: "DEV"
        val artifactDescription = getExtraString("description") ?: ""
        val artifactUrl: String = getExtraString("url") ?: "http://thdev.tech/"

        println("artifactName $artifactName")
        println("libraryVersion $libraryVersion")
        println("artifactDescription $artifactDescription")
        println("artifactUrl $artifactUrl")

        // Configure maven central repository
        repositories {
            maven {
                name = "sonatype"
                setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = getExtraString("ossrhUsername")
                    password = getExtraString("ossrhPassword")
                }
            }
        }

        // Configure all publications
        publications {
            create<MavenPublication>("release") {
                groupId = groupId()
                artifactId = artifactName
                version = libraryVersion

                if (project.plugins.hasPlugin("com.android.library")) {
                    from(components.getByName("release"))
                } else {
                    from(components.getByName("java"))
                }

                // Stub android
                artifact(androidSourceJar.get())
                // Stub javadoc.jar artifact
                artifact(javadocJar.get())

                // Provide artifacts information requited by Maven Central
                pom {
                    name.set(artifactName)
                    description.set(artifactDescription)
                    url.set(artifactUrl)

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("taehwandev")
                            name.set("taehwan")
                            email.set("develop@thdev.tech")
                        }
                    }
                    scm {
                        url.set(artifactUrl)
                    }
                }
            }
        }
    }

    // Signing artifacts. Signing.* extra properties values will be used
    signing {
        useInMemoryPgpKeys(
            getExtraString("signing.keyId"),
            getExtraString("signing.key"),
            getExtraString("signing.password"),
        )
        sign(publishing.publications)
    }
}