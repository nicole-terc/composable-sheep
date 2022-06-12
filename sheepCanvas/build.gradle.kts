plugins {
    id(Plugins.application)
    kotlin(Plugins.android)
    kotlin(Plugins.kapt)
    id(Plugins.parcelize)
    id(Plugins.daggerHilt)
    kotlin(Plugins.serialize)
}

android {
    compileSdk = ModuleExtension.compileSdkVersion
    defaultConfig {
        applicationId = ModuleExtension.App.applicationIdCanvas
        minSdk = ModuleExtension.DefaultConfigs.minSdkVersion
        targetSdk = ModuleExtension.DefaultConfigs.targetSdkVersion
        versionName = ModuleExtension.App.versionName
        versionCode = ModuleExtension.App.versionCode

        testInstrumentationRunner = ModuleExtension.DefaultConfigs.testInstrumentationRunner
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile(ModuleExtension.DefaultConfigs.defaultProguardOptimizeFileName),
                ModuleExtension.DefaultConfigs.proGuardRules
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = ModuleExtension.jvmTarget
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.core.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.design)
    implementation(projects.canvasExtensions)
    implementation(projects.sheep)

    implementation(libs.bundles.coroutines)
    implementation(libs.serialization.json)
    implementation(libs.hilt.core)
    implementation(libs.jetpack.core)
    implementation(libs.jetpack.lifecycle)

    implementation(libs.bundles.compose.jetpack)
    implementation(libs.bundles.accompanist)

    implementation(libs.bundles.room)

    kapt(libs.jetpack.lifecycle.compiler)
    kapt(libs.hilt.compiler)
    kapt(libs.room.compiler)

    debugImplementation(libs.debug.compose.ui.tooling)
}
