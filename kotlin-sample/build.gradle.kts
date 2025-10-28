import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("jvm")
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.ben.manes.versions)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":qr-code"))
    implementation(project(":qr-code-svg"))
}

detekt {
    // Using the latest detekt version (1.23.8) from libs.versions.toml
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
