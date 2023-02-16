@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("jvm") version "1.8.0"
    application
    alias(libs.plugins.shadow)
    alias(libs.plugins.serialization)
    id("org.jetbrains.kotlin.plugin.dataframe") version "0.10.0-dev-1373"
}

group = "com.phodal.chapi"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.serialization.json)

    implementation(libs.dataframe.arrow)

    // for chapi
    implementation(libs.chapi.domain) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-test-junit")
    }
    implementation(libs.chapi.java) {
        exclude(group = "com.ibm.icu", module = "icu4j")
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