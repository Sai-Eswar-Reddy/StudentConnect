plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // Make sure you have this for Firebase
}

android {
    namespace = "com.example.studentconnect"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.studentconnect"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0-alpha03")
    implementation("com.google.android.material:material:1.12.0-alpha03")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.0")

    dependencies {
        implementation ("com.google.android.material:material:1.8.0") // or the latest version
    }


    implementation ("androidx.annotation:annotation:1.7.1") // Use the latest version

    implementation ("com.google.android.material:material:1.11.0") // Use the latest stable version

    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth:22.3.1") // Use the latest version

    // Firebase Realtime Database
    implementation("com.google.firebase:firebase-database:20.3.0") // Add this if you haven't already

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")
}