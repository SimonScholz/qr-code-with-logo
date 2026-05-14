plugins {
    alias(libs.plugins.kotlin.jvm) apply false
}

val copyGitHook by tasks.registering(Copy::class) {
    from("${rootProject.rootDir}/git-hook/pre-push")
    into("${rootProject.rootDir}/.git/hooks")
}

tasks.register<Exec>("installKtlintGitPrePushHook") {
    dependsOn(copyGitHook)

    commandLine(
        "chmod",
        "+x",
        "${rootProject.rootDir}/.git/hooks/pre-push",
    )
}
