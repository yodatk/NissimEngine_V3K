import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.20"
    java
    id("com.github.johnrengelman.shadow") version("6.1.0")
    kotlin("plugin.serialization") version "1.5.20"

    application
}
group = "me.tomer"
version = ""

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.4.2")
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test-junit"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0")
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"



}



application {
    mainClassName = "MainKt"
}