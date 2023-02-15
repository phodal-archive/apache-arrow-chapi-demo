@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "com.phodal.chapi"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.arrow.vector)
    implementation(libs.arrow.memory.netty)

    // for chapi
    implementation(libs.chapi.domain) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-test-junit")
    }
    implementation(libs.chapi.java) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-test-junit")
    }

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("com.phodal.chapi.arrow.MainKt")
}