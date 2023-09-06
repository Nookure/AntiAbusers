plugins {
    id("java")
    kotlin("jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "es.angelillo15"
version = "0.1.0"

dependencies {
    implementation(project(":AntiAbusers-API"))
    implementation(project(":AntiAbusers-Paper"))
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
        maven("https://repo.codemc.io/repository/nms")
        maven("https://repo.nookure.com/releases")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://maven.enginehub.org/repo/")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    dependencies {
        compileOnly(rootProject.libs.nookcore)
    }

    kotlin {
        jvmToolchain(17);
    }
}

tasks.shadowJar {
    archiveFileName.set("AntiAbusers-Paper.jar")
    exclude("kotlin/**")
}