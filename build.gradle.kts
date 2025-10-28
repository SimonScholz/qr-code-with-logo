plugins {
    alias(libs.plugins.kotlin.jvm) apply false
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
