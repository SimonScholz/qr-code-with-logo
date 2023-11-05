import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("jvm")
    application

    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")

    id("org.graalvm.buildtools.native") version "0.9.28"
}

distributions {
    main {
        distributionBaseName = "qr-code-with-logo-app"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":qr-code"))

    implementation("com.miglayout:miglayout-swing:11.2")
    implementation("org.eclipse.platform:org.eclipse.core.databinding:1.13.100")
    // Just for comparison with JFace implementation
    // implementation("org.eclipse.platform:org.eclipse.jface.databinding:1.15.100")
}

application {
    mainClass = "io.github.simonscholz.MainKt"
}

graalvmNative {
    binaries {
        named("main") {
            imageName.set("qr-code-app")
            mainClass.set("io.github.simonscholz.MainKt")
            fallback.set(false)
        }
    }
    binaries.all {
        buildArgs.add("-Djava.awt.headless=false")
        buildArgs.add("--verbose")
        buildArgs.add("--no-fallback")
        buildArgs.add("-H:ConfigurationFileDirectories=src/main/resources/META-INF/native-image")
        buildArgs.add("-H:EnableURLProtocols=http,https")
        buildArgs.add("-H:+AddAllCharsets")
        resources.autodetect()
    }
    toolchainDetection = false
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
