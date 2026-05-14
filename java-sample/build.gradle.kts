plugins {
    java
    alias(libs.plugins.ben.manes.versions)
}

group = "com.github.simonscholz"
version = "0.1.0"

repositories {
    maven {
        url = uri("https://central.sonatype.com/repository/maven-snapshots/")
        mavenContent {
            snapshotsOnly()
        }
    }
    mavenCentral()
}

dependencies {
    implementation("io.github.simonscholz:qr-code-with-logo:0.6.0-SNAPSHOT")
    implementation("io.github.simonscholz:qr-code-with-logo-svg:0.6.0-SNAPSHOT")

    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}
