object ModuleExtension {
    const val compileSdkVersion = 31
    const val jvmTarget = "1.8"

    object DefaultConfigs {
        const val minSdkVersion = 23
        const val targetSdkVersion = 31
        const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        const val defaultConsumerProguardFiles = "consumer-rules.pro"
        const val proGuardRules = "proguard-rules.pro"
        const val defaultProguardOptimizeFileName = "proguard-android-optimize.txt"
    }

    object App {
        const val applicationIdCanvas = "nstv.sheep.canvas"
        const val applicationIdAnimations = "nstv.sheep.animations"
        const val versionName = "1.0"
        const val versionCode = 1
    }

    object FilePath {
        const val detekt = "gradle/config/detekt.yml"
    }
}
