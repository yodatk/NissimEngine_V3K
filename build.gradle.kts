import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    java
    id("com.github.johnrengelman.shadow") version("6.1.0")

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
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"



}



application {
    mainClassName = "MainKt"
}