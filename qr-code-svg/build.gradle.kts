import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    `java-library`

    kotlin("jvm")
    alias(libs.plugins.ben.manes.versions)
    alias(libs.plugins.vanniktech.publish)
    alias(libs.plugins.dokka)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":qr-code"))

    implementation(libs.batik.dom)
    implementation(libs.batik.svggen)
    runtimeOnly(libs.batik.codec)

    testImplementation(libs.assertk)
    testImplementation(libs.kotlin.junit)
    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.platform.launcher)
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
}
