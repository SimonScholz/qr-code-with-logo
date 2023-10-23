plugins {
    id("java")
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
    implementation("io.github.simonscholz:qr-code-with-logo:0.1.0")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
}

tasks.test {
    useJUnitPlatform()
}
