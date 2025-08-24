@Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")
plugins {
    alias(libs.plugins.application).apply(false)
    alias(libs.plugins.library).apply(false)
    alias(libs.plugins.kotlin).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.serialization).apply(false)
    alias(libs.plugins.ktlint).apply(true)
    alias(libs.plugins.detekt).apply(true)
}

buildscript {
    dependencies {
        classpath(libs.hilt.classpath)
    }
}

subprojects {
    apply(plugin = Plugins.ktlint)
    apply(plugin = Plugins.detekt)

    detekt {
        config = files("$rootDir/${ModuleExtension.FilePath.detekt}")
        buildUponDefaultConfig = true
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
