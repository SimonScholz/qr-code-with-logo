plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "qr-code-with-logo"
include("qr-code")
include("kotlin-sample")
include("kotlin-app")
