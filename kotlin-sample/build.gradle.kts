plugins {
    kotlin("jvm")
    alias(libs.plugins.ben.manes.versions)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":qr-code"))
    implementation(project(":qr-code-svg"))
}
