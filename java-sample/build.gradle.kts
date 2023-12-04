plugins {
    id("java")
    id("com.github.ben-manes.versions") version "0.50.0"
}

group = "com.github.simonscholz"
version = "0.1.0"

repositories {
    mavenCentral()
    maven(
        url = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
    )
}

dependencies {
    implementation("io.github.simonscholz:qr-code-with-logo:0.3.0-SNAPSHOT")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.1")
}

tasks.test {
    useJUnitPlatform()
}
