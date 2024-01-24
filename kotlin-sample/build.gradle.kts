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

    // only needed during runtime so that the service loader in the qr-code module can find the implementation
    runtimeOnly(project(":qr-code-svg"))
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
