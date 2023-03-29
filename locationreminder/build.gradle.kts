@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
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
    implementation(Libraries.gmsPlayServicesAuth)
    implementation(Libraries.firebaseUIAuth)

    implementation(Libraries.workManagerKtx)

    //Google Map
    implementation(Libraries.playServicesMaps)
    implementation(Libraries.playServicesLocation)

    testImplementation(TestLibraries.junit)
    testImplementation(TestLibraries.androidXJunitKtx)
    testImplementation(TestLibraries.androidXTestCoreKtx)
    testImplementation(TestLibraries.androidXArchCoreTesting)
    testImplementation(TestLibraries.robolectric)
    testImplementation(TestLibraries.truth)
    testImplementation(TestLibraries.coroutine)
    testImplementation(TestLibraries.koinTest)
    testImplementation(TestLibraries.koinTestJUnit4)

    androidTestImplementation(TestLibraries.androidXJunitKtx)
    androidTestImplementation(TestLibraries.androidXArchCoreTesting)
    androidTestImplementation(TestLibraries.espressoCore)
    androidTestImplementation(TestLibraries.espressoContrib)
    androidTestImplementation(TestLibraries.mockitoAndroid)
    androidTestImplementation(TestLibraries.coroutine)
    androidTestImplementation(TestLibraries.truth)
    androidTestImplementation(TestLibraries.koinTest)
    androidTestImplementation(TestLibraries.koinTestJUnit4)

    debugImplementation(TestLibraries.fragmentTesting)
    debugImplementation(TestLibraries.androidXTestCoreKtx)

}