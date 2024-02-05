plugins {
    kotlin("jvm") version "1.9.22" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.5" apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0" apply false
    id("com.github.ben-manes.versions") version "0.51.0" apply false
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
