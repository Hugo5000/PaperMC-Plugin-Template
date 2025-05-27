plugins {
    id("java")
    id("idea")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

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
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
//    mavenLocal()
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.1")
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")

    compileOnly("at.hugob.plugin.library:database:1.0.0")
    compileOnly("at.hugob.plugin.library:config:1.1.2")
    compileOnly("at.hugob.plugin.library:gui:0.0.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    java.targetCompatibility = JavaVersion.VERSION_21
//    withSourcesJar()
//    withJavadocJar()
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


tasks.register<Copy>("prepareServer") {
    dependsOn("build")
    from(tasks.jar.get().archiveFile.get().asFile.path)
    rename(tasks.jar.get().archiveFile.get().asFile.name, "${rootProject.name}.jar")
    into("G:\\Minecraft Servers\\paper\\plugins")
}

tasks {
    compileJava {
        options.compilerArgs.add("-parameters")
        options.encoding = "UTF-8"
    }
    compileTestJava { options.encoding = "UTF-8" }
    javadoc { options.encoding = "UTF-8" }
    // plugin.yml placeholders
    processResources {
        outputs.upToDateWhen { false }
        filesMatching("**/*plugin.yml") {
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