plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }

android {
    namespace = "com.haseltonmediagroup.darts3d"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.haseltonmediagroup.darts3d"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1-prototype"
    }
}

dependencies { implementation("androidx.core:core-ktx:1.15.0"); implementation("androidx.appcompat:appcompat:1.7.0") }
