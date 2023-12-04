plugins {
    kotlin("jvm") version "1.9.21" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.4" apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.0.2" apply false
    id("com.github.ben-manes.versions") version "0.50.0" apply false
}

tasks.register("installKtlintGitPrePushHook", Copy::class) {
    from("${rootProject.rootDir}/git-hook/pre-push")
    into("${rootProject.rootDir}/.git/hooks")
    doLast {
        exec {
            commandLine("chmod", "+x", "${rootProject.rootDir}/.git/hooks/pre-push")
        }
    }
}
