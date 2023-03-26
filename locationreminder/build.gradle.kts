@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.udacity.locationreminder"
    compileSdk = Config.compileSdkVersion

    defaultConfig {
        applicationId = "com.udacity.locationreminder"
        minSdk = 21
        targetSdk = Config.targetSdkVersion
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    //App dependencies
    implementation(Libraries.androidxCore)
    implementation(Libraries.androidxAppcompat)
    implementation(Libraries.material)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.swiperefreshlayout)
    implementation(Libraries.timber)

    //Architecture components
    implementation(Libraries.androidxNavigationUi)
    implementation(Libraries.androidxNavigationFragment)
    implementation(Libraries.androidxRoomRuntime)
    implementation(Libraries.androidxRoomKtx)
    kapt(Libraries.androidxRoomCompiler)
    implementation(Libraries.koinAndroid)

    //Firebase
    implementation(platform(Libraries.firebaseBom))
    implementation(Libraries.firebaseAuthKtx)
    implementation(Libraries.firebaseMessagingKtx)

    //Google Map
    implementation(Libraries.playServicesMaps)

    testImplementation(TestLibraries.junit)
    androidTestImplementation(TestLibraries.androidXJunitKtx)
    androidTestImplementation(TestLibraries.espressoCore)
}