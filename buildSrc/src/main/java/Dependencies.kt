object Libraries {
    const val androidxCore = "androidx.core:core-ktx:1.9.0"
    const val androidxAppcompat = "androidx.appcompat:appcompat:1.5.1"
    const val material = "com.google.android.material:material:1.7.0"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
    const val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"

    const val lifecyleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.4.0"
    const val lifecyleKtx = "androidx.lifecycle:lifecycle-extensions:2.4.0"
    const val lifecyleLivedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:2.6.0"


    const val preferences = "androidx.preference:preference:1.2.0"

    //Timber
    const val timber = "com.jakewharton.timber:timber:5.0.1"

    //Navigation
    const val androidxNavigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navVersion}"
    const val androidxNavigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navVersion}"

    //Room
    const val androidxRoomRuntime = "androidx.room:room-runtime:${Versions.roomVersion}"
    const val androidxRoomCompiler = "androidx.room:room-compiler:${Versions.roomVersion}"
    const val androidxRoomKtx = "androidx.room:room-ktx:${Versions.roomVersion}"

    //Moshi
    const val moshi = "com.squareup.moshi:moshi:1.14.0"
    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:1.13.0"

    //Coil
    const val coil = "io.coil-kt:coil:1.1.1"

    //Koin
    const val koinAndroid = "io.insert-koin:koin-android:${Versions.koinVersion}"
    const val koinAndroidXScope =  "org.koin:koin-androidx-scope:${Versions.koinVersion}"
    const val koinViewmodel = "org.koin:koin-androidx-viewmodel:${Versions.koinVersion}"
    const val koinAndroidXNavigation = "io.insert-koin:koin-androidx-navigation:${Versions.koinVersion}"

    //Glide
    const val glide = "com.github.bumptech.glide:glide:4.15.1"

    //Kotlin Serialization
    const val kotlinxSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0"

    //Retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
    const val retrofitScalarsConverter =
        "com.squareup.retrofit2:converter-scalars:${Versions.retrofitVersion}"
    const val retrofitMoshiConverter =
        "com.squareup.retrofit2:converter-moshi:${Versions.retrofitVersion}"
    const val retrofitKotlinxSerializationConverter =
        "jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"

    //Work Manager
    const val workManagerKtx = "androidx.work:work-runtime-ktx:${Versions.workVersion}"

    //Firebase
    const val firebaseBom = "com.google.firebase:firebase-bom:31.2.2"
    const val firebaseMessagingKtx = "com.google.firebase:firebase-messaging-ktx"
    const val firebaseAuthKtx = "com.google.firebase:firebase-auth-ktx"
    const val firebaseUIAuth = "com.firebaseui:firebase-ui-auth:7.2.0"

    //Google Play Services
    const val playServicesMaps = "com.google.android.gms:play-services-maps:18.0.2"
    const val gmsPlayServicesAuth = "com.google.android.gms:play-services-auth:20.4.1"
    const val playServicesLocation = "com.google.android.gms:play-services-location:21.0.1"

}

object TestLibraries {
    const val junit = "junit:junit:4.13.2"
    const val androidXJunitKtx = "androidx.test.ext:junit-ktx:1.1.5"
    const val androidXTestCoreKtx = "androidx.test:core-ktx:1.5.0"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoVersion}"
    const val espressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.espressoVersion}"

    // required if you want to use Mockito for unit tests
    const val mockitoCore = "org.mockito:mockito-core:5.2.0"
    // required if you want to use Mockito for Android tests
    const val mockitoAndroid =  "org.mockito:mockito-android:5.2.0"

    const val androidXArchCoreTesting = "androidx.arch.core:core-testing:2.2.0"
    const val room = "androidx.room:room-testing:${Versions.roomVersion}"
    const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4"

    const val robolectric = "org.robolectric:robolectric:4.9.2"
    const val truth = "com.google.truth:truth:1.1.3"

    const val koinTestJUnit4 = "io.insert-koin:koin-test-junit4:${Versions.koinVersion}"

    const val fragmentTesting = "androidx.fragment:fragment-testing:1.5.5"
}


