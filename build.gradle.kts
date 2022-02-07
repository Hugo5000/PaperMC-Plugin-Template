plugins {
    id("java")
    id("idea")
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

val githubUsername: String by project
val githubToken: String by project

val pluginName: String by project
val pluginVersion: String by project
val pluginApi: String by project
val pluginDescription: String by project
val pluginGroup: String by project
val pluginArtifact: String by project
val pluginMain: String by project

group = pluginGroup
version = pluginVersion

repositories {
    mavenCentral()
    // paper-api
    maven("https://papermc.io/repo/repository/maven-public/")
    // ADKUtils
    maven {
        url = uri("https://maven.pkg.github.com/Advanced-Kind-MC/ADKUtils")
        credentials {
            username = githubUsername
            password = githubToken
        }
    }
    mavenLocal()
}

dependencies {
    compileOnly("com.advancedkind.plugin:utils:1.2.3")
    compileOnly("org.jetbrains:annotations:20.1.0")
    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")

}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    java.targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}
sourceSets {
    main {
        java {
            srcDir("src")
        }
        resources {
            srcDir("resources")
        }
    }
    test {
        java {
            srcDir("test")
        }
    }
}
idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        relocate("co.aikar.commands", "${pluginGroup}.${pluginArtifact}.acf")
        relocate("co.aikar.locales", "${pluginGroup}.${pluginArtifact}.locales")
    }
    compileJava {
        options.compilerArgs.add("-parameters")
        options.encoding = "UTF-8"
    }
    compileTestJava { options.encoding = "UTF-8" }
    javadoc { options.encoding = "UTF-8" }
    build {
        dependsOn(shadowJar)
    }
    runServer {
        dependsOn(build)
        minecraftVersion("1.17.1")
    }
    // plugin.yml placeholders
    processResources {
        outputs.upToDateWhen { false }
        filesMatching("**/plugin.yml") {
            expand(
                mapOf(
                    "version" to pluginVersion,
                    "api" to pluginApi,
                    "name" to pluginName,
                    "artifact" to pluginArtifact,
                    "main" to pluginMain,
                    "description" to pluginDescription,
                    "group" to pluginGroup
                )
            )
        }
    }
}
