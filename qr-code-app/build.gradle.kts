import io.gitlab.arturbosch.detekt.Detekt

plugins {
    application
    kotlin("jvm")

    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.ben.manes.versions)

    alias(libs.plugins.jlink)
    alias(libs.plugins.shadow)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":qr-code"))
    implementation(project(":qr-code-svg"))

    implementation(libs.miglayout)
    implementation(libs.databinding) {
        exclude(group = "org.eclipse.platform", module = "org.eclipse.osgi")
    }
    implementation(libs.datePicker)
    implementation(libs.swingx)
    implementation(libs.jackson.kotlin)
    implementation(libs.kotlinpoet.javapoet)

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
