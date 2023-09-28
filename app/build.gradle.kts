import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
}

android {
    namespace = "com.example.rickandmorty"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.rickandmorty"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }



    dependencies {

        // Room Database
        implementation("androidx.room:room-runtime:2.5.2")
        kapt("androidx.room:room-compiler:2.5.2")
        implementation("androidx.room:room-ktx:2.5.2")
        implementation("androidx.room:room-paging:2.5.2")


        // Navigation Components
        implementation("androidx.navigation:navigation-fragment-ktx:2.7.2")
        implementation("androidx.navigation:navigation-ui-ktx:2.7.2")

        // AndroidX Libraries
        implementation("androidx.fragment:fragment-ktx:1.6.1")
        implementation("androidx.viewpager2:viewpager2:1.0.0")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
        implementation("androidx.activity:activity-ktx:1.7.2")
        implementation("androidx.core:core-ktx:1.12.0")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.recyclerview:recyclerview:1.3.1")

        // Material Design Components
        implementation("com.google.android.material:material:1.9.0")

        // Koin (Dependency Injection)
        implementation("io.insert-koin:koin-android:3.3.0")

        // Glide (Image Loading)
        implementation("com.github.bumptech.glide:glide:4.15.1")
        annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")

        // Retrofit (Networking)
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")

        // JavaFaker (Random Data Generator)
        implementation("com.github.javafaker:javafaker:1.0.2")

        // Kotlin Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")


        // AppCompat & Other AndroidX Libraries
        implementation("androidx.appcompat:appcompat:1.6.1")
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

        // AutofitTextView
        implementation("me.grantland:autofittextview:0.2.1")

        //Paging 
        implementation("androidx.paging:paging-common-ktx:3.2.1")
        implementation("androidx.paging:paging-runtime-ktx:3.2.1")



    }
}

