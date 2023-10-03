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
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
