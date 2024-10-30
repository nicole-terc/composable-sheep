object ModuleExtension {
    const val compileSdkVersion = 34
    const val jvmTarget = "11"

    object DefaultConfigs {
        const val minSdkVersion = 23
        const val targetSdkVersion = 34
        const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        const val defaultConsumerProguardFiles = "consumer-rules.pro"
        const val proGuardRules = "proguard-rules.pro"
        const val defaultProguardOptimizeFileName = "proguard-android-optimize.txt"
    }

    object App {
        const val applicationIdCanvas = "nstv.sheep.canvas"
        const val applicationIdAnimations = "nstv.sheep.animations"
        const val versionName = "1.1"
        const val versionCode = 2
    }

    object FilePath {
        const val detekt = "gradle/config/detekt.yml"
    }
}
