//TODO: get plugins from version catalog once this issue is tackled: https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    id("com.android.application").version("7.1.1").apply(false)
    id("com.android.library").version("7.1.1").apply(false)
    id("org.jetbrains.kotlin.android").version("1.6.10").apply(false)
    kotlin("plugin.serialization") version "1.6.10"
}

buildscript{
    dependencies{
        classpath(libs.hilt.classpath)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
