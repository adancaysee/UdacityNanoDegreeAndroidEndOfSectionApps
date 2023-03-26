plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs")
}
@Suppress("UnstableApiUsage")
android {
    namespace = "com.udacity.politicalpreparedness"
    compileSdk = Config.compileSdkVersion

    defaultConfig {
        applicationId = "com.udacity.politicalpreparedness"
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

    //Retrofit
    implementation(Libraries.moshiKotlin)
    implementation(Libraries.retrofitMoshiConverter)

    //Coil
    implementation(Libraries.coil)

    testImplementation(TestLibraries.junit)
    androidTestImplementation(TestLibraries.androidXJunitKtx)
    androidTestImplementation(TestLibraries.espressoCore)
}