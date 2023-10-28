import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("jvm")

    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":qr-code"))

    implementation("com.miglayout:miglayout-swing:11.2")
    implementation("org.eclipse.platform:org.eclipse.core.databinding:1.13.100")
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
