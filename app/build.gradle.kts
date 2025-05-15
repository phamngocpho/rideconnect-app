import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.ksp)
}

// Read local.properties
val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        FileInputStream(localPropertiesFile).use { this.load(it) }
    }
}

// Helper function to safely read properties
fun getLocalProperty(key: String, defaultValue: String = ""): String {
    return localProperties.getProperty(key, defaultValue)
}

android {
    namespace = "com.rideconnect"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rideconnect"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "API_BASE_URL", "\"${getLocalProperty("api.base.url", "")}\"")
            buildConfigField("String", "GOONG_API_BASE_URL", "\"${getLocalProperty("goong.api.base.url", "")}\"")
            buildConfigField("String", "GOONG_API_KEY", "\"${getLocalProperty("goong.api.key", "")}\"")
            buildConfigField("String", "GOONG_MAPTILES_KEY", "\"${getLocalProperty("goong.maptiles.key", "")}\"")
            buildConfigField("String", "MAPBOX_ACCESS_TOKEN", "\"${getLocalProperty("MAPBOX_ACCESS_TOKEN", "")}\"")
        }

        debug {
            buildConfigField("String", "API_BASE_URL", "\"${getLocalProperty("api.base.url", "")}\"")
            buildConfigField("String", "GOONG_API_BASE_URL", "\"${getLocalProperty("goong.api.base.url", "")}\"")
            buildConfigField("String", "GOONG_API_KEY", "\"${getLocalProperty("goong.api.key", "")}\"")
            buildConfigField("String", "GOONG_MAPTILES_KEY", "\"${getLocalProperty("goong.maptiles.key", "")}\"")
            buildConfigField("String", "MAPBOX_ACCESS_TOKEN", "\"${getLocalProperty("MAPBOX_ACCESS_TOKEN", "")}\"")
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
        viewBinding = true// Required to use BuildConfig
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "2.0.21"
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-Xlint:deprecation")
    }
}

dependencies {
    // Dependencies remain unchanged
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // New
    implementation(libs.androidx.navigation.compose)

    // Constranlayout
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.1.1")

    // Retrofit for API calls
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Dependency Injection - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // DataStore Preferences
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.preferences.core)

    // Location Services
    implementation(libs.play.services.location)

    // Maps (Goong Map SDK)
    implementation("com.github.goong-io:goong-map-android-sdk:1.5@aar") {
        isTransitive = true
    }

    // WebSocket
    implementation(libs.okhttp)

    // Image Loading
    implementation(libs.coil.compose)

    // Icon
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)

    implementation (libs.logging.interceptor)

    implementation (libs.android)

    implementation (libs.maps.compose)

    implementation ("com.mapbox.base:common:0.11.0")
    implementation ("com.mapbox.mapboxsdk:mapbox-android-gestures:0.8.0")

    // Thư viện hỗ trợ GeoJSON và Turf
    implementation ("com.mapbox.mapboxsdk:mapbox-sdk-geojson:5.4.1")
    implementation ("com.mapbox.mapboxsdk:mapbox-sdk-turf:5.4.1")

    implementation ("com.mapbox.plugin:maps-lifecycle:11.11.0")
    implementation ("com.mapbox.plugin:maps-compass:11.11.0")
    implementation ("com.mapbox.plugin:maps-logo:11.11.0")
    implementation ("com.mapbox.plugin:maps-scalebar:11.11.0")
    implementation ("com.mapbox.plugin:maps-gestures:11.11.0")
    implementation ("com.mapbox.plugin:maps-attribution:11.11.0")

    implementation ("com.mapbox.navigationcore:android:3.9.0-rc.1")

}
