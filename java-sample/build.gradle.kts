plugins {
    id("java")
}

group = "com.github.simonscholz"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":qr-code"))
}

tasks.test {
    useJUnitPlatform()
}
