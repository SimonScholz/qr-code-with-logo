import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.vanniktech.maven.publish.SonatypeHost
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("jvm")
    `java-library`

    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.github.ben-manes.versions") version "0.49.0"
    id("com.vanniktech.maven.publish") version "0.25.3"
    id("org.jetbrains.dokka") version "1.9.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.zxing:core:3.5.2")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.1")

    testImplementation("com.google.zxing:javase:3.5.2")
    testImplementation("com.willowtreeapps.assertk:assertk:0.27.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.9.10")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.0")
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

detekt {
    toolVersion = "1.23.1"
    config.setFrom(file("${project.rootDir}/config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
}

tasks.withType<Detekt>().configureEach {
    reports {
        xml.required.set(false)
        html.required.set(true)
        txt.required.set(false)
        sarif.required.set(false)
        md.required.set(false)
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.S01)

    signAllPublications()
}
