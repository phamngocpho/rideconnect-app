
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Thêm Hilt Gradle plugin
        classpath("com.google.dagger:hilt-android-gradle-plugin:${libs.versions.hiltAndroid.get()}")

        // Thêm Navigation Safe Args plugin
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${libs.versions.navigationSafeArgs.get()}")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}
