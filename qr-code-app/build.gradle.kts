import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("jvm")
    application

    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.github.ben-manes.versions")

    id("org.graalvm.buildtools.native") version "0.9.28"
    id("com.ryandens.jlink-application") version "0.4.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":qr-code"))

    implementation("com.miglayout:miglayout-swing:11.3")
    implementation("org.eclipse.platform:org.eclipse.core.databinding:1.13.100") {
        exclude(group = "org.eclipse.platform", module = "org.eclipse.osgi")
    }
    implementation("com.github.lgooddatepicker:LGoodDatePicker:11.2.1")
    implementation("org.swinglabs:swingx:1.6.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
    implementation("com.squareup:kotlinpoet-javapoet:1.16.0")

    // implementation("io.quarkus:quarkus-awt-deployment:3.5.0")

    // Just for comparison with JFace implementation
    // implementation("org.eclipse.platform:org.eclipse.jface.databinding:1.15.100")
}

distributions {
    main {
        distributionBaseName = "qr-code-with-logo-app"
    }
}

application {
    mainClass = "io.github.simonscholz.MainKt"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

jlinkJre {
    // defaults to only java.base
    modules.set(setOf("java.desktop", "jdk.charsets", "java.compiler"))
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
        // https://www.graalvm.org/latest/reference-manual/native-image/dynamic-features/Resources/
        buildArgs.add("-H:IncludeResources=.*png\$")
        buildArgs.add("-H:ResourceConfigurationFiles=${layout.projectDirectory}/resource-config.json")
        buildArgs.add("-H:ReflectionConfigurationFiles=${layout.projectDirectory}/reflection-config.json")
        buildArgs.add("-H:EnableURLProtocols=http,https")
        buildArgs.add("-H:+AddAllCharsets")

        resources { autodetect() }
    }
    toolchainDetection = false
}

tasks.register("nativeDist") {
    dependsOn("nativeCompile")

    doLast {
        copy {
            into(layout.buildDirectory.dir("dist"))
            from(layout.buildDirectory.dir("native/nativeCompile"))
//            include("*.exe")
//            include("*.dll")
        }

        println(System.getProperty("java.home"))

        copy {
            into(layout.buildDirectory.dir("dist/lib"))
            from(System.getProperty("java.home") + "/lib")
            include("fontconfig.bfc")
            include("fontconfig.properties.src")
            include("psfont.properties.ja")
            include("psfontj2d.properties")
        }
    }
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
