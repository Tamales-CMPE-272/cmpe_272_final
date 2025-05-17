plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
    namespace = "com.example.tamaleshr"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tamaleshr"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    debugImplementation(libs.androidx.fragment.testing)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.adapters)
    implementation(libs.converter.moshi)
    implementation(libs.logging.interceptor)

    // Date time
    implementation(libs.joda.time)

    // encrypted shared preferences
    implementation(libs.androidx.security.crypto)

    testImplementation(libs.junit)
    testImplementation(libs.robolectric)

    // Koin testing tools
    testImplementation(libs.koin.test)
    // Needed JUnit version
    testImplementation(libs.koin.test.junit4)
    testImplementation(libs.truth)

    testImplementation(libs.mockk)

    // MockK for Android instrumentation tests (if needed)
    androidTestImplementation(libs.mockk.android)

    testImplementation(libs.androidx.core)
    testImplementation(libs.androidx.fragment.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)
}