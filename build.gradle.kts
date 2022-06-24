// TODO: get plugins from version catalog once this issue is tackled: https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")
plugins {
    id("com.android.application").version("7.1.1").apply(false)
    id("com.android.library").version("7.1.1").apply(false)
    id("org.jetbrains.kotlin.android").version("1.6.10").apply(false)
    kotlin("plugin.serialization") version "1.6.10"
    id(Plugins.ktlint).version(libs.plugins.ktlint.get().version.toString())
    id(Plugins.detekt).version(libs.plugins.detekt.get().version.toString())
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

    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-Xopt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-Xopt-in=androidx.compose.animation.ExperimentalAnimationApi",
            )
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
