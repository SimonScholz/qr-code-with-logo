import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("jvm")

    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.github.ben-manes.versions")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":qr-code"))
}

detekt {
    toolVersion = "1.23.4"
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
